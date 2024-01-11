package com.example.automaat.api.synchers

import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.concurrent.CountDownLatch

class RentalSyncManager(private val rentalRepository: RentalRepository) : ISyncManager {
    override fun syncEntities() {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {

                //sync remote rentals to local
                val jsonArray = Rentals().getAllRentals()
                jsonArray?.forEach { jsonElement ->
                    jsonElement.asJsonObject.let {
                        val carJsonObject =
                            if (it.has("car") && it.get("car").isJsonObject) it.getAsJsonObject(
                                "car"
                            ) else null
                        val carId =
                            carJsonObject?.takeIf { it.has("id") && it.get("id").isJsonPrimitive }
                                ?.get("id")?.asInt

                        val customerJsonObject =
                            if (it.has("customer") && it.get("customer").isJsonObject) it.getAsJsonObject(
                                "customer"
                            ) else null
                        val customerId =
                            customerJsonObject?.takeIf { it.has("id") && it.get("id").isJsonPrimitive }
                                ?.get("id")?.asInt
                        println(it)
                        val remoteRental = RentalModel(
                            it.get("id").asInt,
                            it.get("code").asString,
                            it.get("longitude").asFloat,
                            it.get("latitude").asFloat,
                            it.get("fromDate").asString,
                            it.get("toDate").asString,
                            RentalState.fromString(it.get("state").asString),
                            null,
                            customerId,
                            carId
                        )

                        val localRental = rentalRepository.getRentalById(remoteRental.id)

                        if (localRental != null && isConflict(localRental, remoteRental)) {
                            resolveConflict(localRental, remoteRental)
                        } else {
                            //check if rental is relevant to user
                            // TODO Hardcoded customer id!!!
                            if (remoteRental.customerId != 1) {
                                return@forEach
                            }
                            rentalRepository.addRental(remoteRental)
                        }

                        Log.i("CHECK_RESPONSE", "Rental added: $remoteRental")
                    }
                }

                val allLocalRentals = rentalRepository.getAll()

                //sync local rentals to server
                allLocalRentals.forEach { localRental ->
                    // rental does not have a car, so it is not relevant
                    if (localRental.carId == null) {
                        return@forEach
                    }

                    // check if this rental is already on the server
                    val allRentals = Rentals().getAllRentals()

                    //find rental in allRentals using car.id
                    val foundRental = allRentals?.find { rental ->
                        rental.asJsonObject.get("car").asJsonObject.get("id").asInt == localRental.carId
                    }?.asJsonObject

                    val deserializedRental = foundRental?.let {
                        val carJsonObject =
                            if (it.has("car") && it.get("car").isJsonObject) it.getAsJsonObject(
                                "car"
                            ) else null
                        val carId =
                            carJsonObject?.takeIf { it.has("id") && it.get("id").isJsonPrimitive }
                                ?.get("id")?.asInt

                        val customerJsonObject =
                            if (it.has("customer") && it.get("customer").isJsonObject) it.getAsJsonObject(
                                "customer"
                            ) else null
                        val customerId =
                            customerJsonObject?.takeIf { it.has("id") && it.get("id").isJsonPrimitive }
                                ?.get("id")?.asInt

                        RentalModel(
                            it.get("id").asInt,
                            it.get("code").asString,
                            it.get("longitude").asFloat,
                            it.get("latitude").asFloat,
                            it.get("fromDate").asString,
                            it.get("toDate").asString,
                            RentalState.fromString(it.get("state").asString),
                            null,
                            customerId,
                            carId
                        )
                    }


                    val rentalModel = RentalModel(
                        localRental.id,
                        localRental.code,
                        localRental.longitude,
                        localRental.latitude,
                        localRental.fromDate,
                        localRental.toDate,
                        localRental.state,
                        null,
                        localRental.customerId,
                        localRental.carId
                    )

                    val rentalWithCarWithCustomer =
                        rentalRepository.getRentalsWithCarAndCustomerByRentalAsync(rentalModel.id)

                    if (deserializedRental == null) {
                        //rental was not found on server, create it
                        Rentals().addRental(rentalWithCarWithCustomer)

                        Log.i("CHECK_RESPONSE", "Rental created: $rentalModel")
                    } else {
                        //rental is already on server, check if there is a conflict
                        if (isConflict(localRental, rentalModel)) {
                            rentalWithCarWithCustomer.rental?.let {
                                println("Conflict detected")
                                resolveConflict(
                                    localRental,
                                    it
                                )
                            }
                        } else {
                            Rentals().updateRental(rentalWithCarWithCustomer)
                            notifyCustomerSuccess(localRental)
                            Log.i("CHECK_RESPONSE", "Rental updated: $rentalModel")
                        }
                    }
                }
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

        rentalRepository.updateRental(remoteRental)
    }

    private fun notifyCustomerConflict(rental: RentalModel) {
        //TODO implement
    }

    private fun notifyCustomerSuccess(rental: RentalModel) {
        //TODO implement
    }
}
