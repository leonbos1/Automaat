package com.example.automaat.api.synchers

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
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
                syncRemoteRentalsToLocal()
                syncLocalRentalsToServer()
            }
        }
    }

    private fun isConflict(localRental: RentalModel, remoteRental: RentalModel): Boolean {
        if (localRental.state == RentalState.RESERVED && remoteRental.state == RentalState.RESERVED) {
            if (localRental.customerId != remoteRental.customerId) {
                return true
            }
        }
        return false
    }

    private suspend fun resolveConflict(localRental: RentalModel, remoteRental: RentalModel) {
        notifyCustomerConflict(localRental)
        println(localRental.id)
        println(remoteRental.id)

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
        println("CONFLICT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
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

            val carsFromLocalDatabase = rentalRepository.getByCarId(remoteRental.carId!!)

            var localRental: RentalModel? = null
            if (carsFromLocalDatabase.isNotEmpty()) {
                localRental = carsFromLocalDatabase[0]
            }

            if (localRental != null) {
                if (isConflict(localRental, remoteRental)) {
                    resolveConflict(localRental, remoteRental)
                } else {
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
        val carObj = json.get("car")?.takeIf { it.isJsonObject }?.asJsonObject
        val carId = carObj?.get("id")?.asInt
        val customerId = customerObj?.get("id")?.asInt

        var r = RentalModel(
            id = json.get("id").asInt,
            code = json.get("code").asString,
            0.0f,
            0.0f,
            fromDate = json.get("fromDate").asString,
            toDate = json.get("toDate").asString,
            state = RentalState.valueOf(json.get("state").asString),
            inspections = null,
            customerId = customerId,
            carId = carId
        )

        if (json.get("longitude") != null && !json.get("longitude").isJsonNull()) {
            r.longitude = json.get("longitude").getAsFloat();
        }
        if (json.get("latitude") != null && !json.get("latitude").isJsonNull()) {
            r.latitude = json.get("latitude").getAsFloat();
        }

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
            if (rental.asJsonObject.get("car")?.asJsonObject?.get("id")?.asInt == carId) {
                return parseJsonToRentalModel(rental.asJsonObject)
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
