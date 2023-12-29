package com.example.automaat.api.synchers

import android.util.Log
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.repositories.RentalRepository
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RentalSyncManager(private val rentalRepository: RentalRepository) : ISyncManager {
    override fun syncEntities(jsonArray: JsonArray) {
        CoroutineScope(Dispatchers.IO).launch {
            jsonArray.forEach { jsonElement ->
                jsonElement.asJsonObject.let {
                    val carJsonObject = if (it.has("car") && it.get("car").isJsonObject) it.getAsJsonObject("car") else null
                    val carId = carJsonObject?.takeIf { it.has("id") && it.get("id").isJsonPrimitive }?.get("id")?.asInt

                    val rentalModel = RentalModel(
                        it.get("id").asInt,
                        it.get("code").asString,
                        it.get("longitude").asFloat,
                        it.get("latitude").asFloat,
                        it.get("fromDate").asString,
                        it.get("toDate").asString,
                        RentalState.fromString(it.get("state").asString),
                        null,
                        null,
                        carId
                    )

                    rentalRepository.addRental(rentalModel)
                    Log.i("CHECK_RESPONSE", "Rental added: $rentalModel")
                }
            }
        }
    }
}