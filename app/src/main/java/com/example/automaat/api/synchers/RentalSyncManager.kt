package com.example.automaat.api.synchers

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.repositories.RentalRepository
import com.example.automaat.utils.NotificationManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentalSyncManager(private val rentalRepository: RentalRepository, application: Application) : ISyncManager {
    private val notificationManager = NotificationManager(application)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun syncEntities() {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    syncRemoteRentalsToLocal()
                    syncLocalRentalsToServer()
                } catch (e: Exception) {
                    Log.e("CHECK_RESPONSE", "Error while syncing rentals", e)
                }
            }
        }
    }

    private fun isConflict(localRental: RentalModel, remoteRental: RentalModel): Boolean {
        if (localRental.state == RentalState.RESERVED && remoteRental.state == RentalState.RESERVED) {
            if (localRental.customerId != remoteRental.customerId && remoteRental.customerId != null) {
                return true
            }
        }
        return false
    }

    private suspend fun resolveConflict(localRental: RentalModel, remoteRental: RentalModel) {
        notifyCustomerConflict(localRental)

        localRental.state = remoteRental.state
        localRental.customerId = remoteRental.customerId
        localRental.fromDate = remoteRental.fromDate
        localRental.toDate = remoteRental.toDate
        localRental.longitude = remoteRental.longitude
        localRental.latitude = remoteRental.latitude

        rentalRepository.updateRental(localRental)
    }

    private fun notifyCustomerConflict(rental: RentalModel) {
        //TODO implement
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun notifyCustomerSuccess(rental: RentalModel) {
        val state = rental.state
        println("LogNotification Send notification. Rental state: $state")
        notificationManager.sendNotification(
            channelId = "rental_updates",
            title = "Rental Update",
            message = "Rental state changed to: $state",
            icon = 1,
            notificationId = (100000..999999).random()
        )
    }

    suspend fun syncRemoteRentalsToLocal() {
        val remoteRentals = Rentals().getAllRentals() ?: return

        for (rental in remoteRentals) {
            var remoteRental = parseJsonToRentalModel(rental as JsonObject)

            val carFromLocalDatabase = rentalRepository.getByCarId(remoteRental.carId)

            var localRental: RentalModel? = null
            if (carFromLocalDatabase.isNotEmpty()) {
                localRental = carFromLocalDatabase[0]
            }

            if (localRental != null) {
                if (isConflict(localRental, remoteRental)) {
                    resolveConflict(localRental, remoteRental)
                } else if (localRental.state != remoteRental.state && localRental.state == RentalState.RETURNED || localRental.state == null) {
                    updateLocalDatabase(remoteRental)
                }
            } else if (isRentalRelevant(remoteRental)) {
                addRentalToLocalDatabase(remoteRental)
            }
        }
    }

    suspend fun addRentalToLocalDatabase(rental: RentalModel) {
        rentalRepository.addRental(rental)
    }

    fun isRentalRelevant(rental: RentalModel): Boolean {
        return rental.carId != null
    }

    suspend fun updateLocalDatabase(rental: RentalModel) {
        rentalRepository.updateRental(rental)
    }

    fun parseJsonToRentalModel(json: JsonObject): RentalModel {
        val customerObj = json.get("customer")?.takeIf { it.isJsonObject }?.asJsonObject
        val customerId = customerObj?.get("id")?.asInt

        val carObj = json.get("car")?.takeIf { it.isJsonObject }?.asJsonObject
        val carId = carObj?.get("id")?.asInt

        val inspectionObj = json.get("inspection")?.takeIf { it.isJsonObject }?.asJsonObject
        val inspectionId = inspectionObj?.get("id")?.asInt

        val id = json.get("id")?.takeIf { it.isJsonPrimitive }?.asInt ?: 0
        val code = json.get("code")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val fromDate = json.get("fromDate")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val toDate = json.get("toDate")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val state = json.get("state")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val longitude = json.get("longitude")?.takeIf { it.isJsonPrimitive }?.asFloat ?: 0.0f
        val latitude = json.get("latitude")?.takeIf { it.isJsonPrimitive }?.asFloat ?: 0.0f


        var r = RentalModel(
            id = id,
            code = code,
            longitude = longitude,
            latitude = latitude,
            fromDate = fromDate,
            toDate = toDate,
            state = RentalState.valueOf(state),
            inspectionId = inspectionId,
            customerId = customerId,
            carId = carId
        )

        return r
    }

    suspend fun createRentalOnServer(rental: RentalModel) {
        val localRentalWithCarWithCustomer =
            rentalRepository.getRentalsWithCarAndCustomerByRentalAsync(rental.id)

        Rentals().addRental(localRentalWithCarWithCustomer)
    }

    suspend fun updateRentalOnServer(rental: RentalModel) {
        val localRentalWithCarWithCustomer =
            rentalRepository.getRentalsWithCarAndCustomerByRentalAsync(rental.id)

        Rentals().updateRental(localRentalWithCarWithCustomer)
    }

    fun getRentalFromServerByCarId(carId: Int, remoteRentals: JsonArray): RentalModel? {
        for (rental in remoteRentals) {
            try {
                if (rental != null && rental.isJsonObject) {
                    val rentalObj = rental.asJsonObject
                    val carObj = rentalObj.get("car")
                    if (carObj != null && carObj.isJsonObject) {
                        val carIdObj = carObj.asJsonObject.get("id")
                        if (carIdObj != null && carIdObj.isJsonPrimitive && carIdObj.asInt == carId) {
                            return parseJsonToRentalModel(rentalObj)
                        }
                    }
                }
            } catch (e: Exception) {
                // Log the exception for debugging purposes
                e.printStackTrace()
            }
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun syncLocalRentalsToServer() {
        val allLocalRentals = rentalRepository.getAll()
        val allRemoteRentals = Rentals().getAllRentals()

        for (rental in allLocalRentals) {
            if (!isRentalRelevant(rental)) {
                continue
            }

            val serverRental = getRentalFromServerByCarId(rental.carId!!, allRemoteRentals!!)

            if (serverRental == null) {
                createRentalOnServer(rental)
                notifyCustomerSuccess(rental)
            } else {
                if (isConflict(rental, serverRental)) {
                    resolveConflict(rental, serverRental)
                } else {
                    updateRentalOnServer(rental)
                    notifyCustomerSuccess(rental)
                }
            }
        }
    }
}
