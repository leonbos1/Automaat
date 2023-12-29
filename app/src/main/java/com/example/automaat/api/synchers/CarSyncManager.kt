package com.example.automaat.api.synchers

import android.util.Log
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.repositories.CarRepository
import com.google.gson.JsonArray
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarSyncManager(private val carRepository: CarRepository) : ISyncManager {
    override fun syncEntities(jsonArray: JsonArray) {
        CoroutineScope(Dispatchers.IO).launch {
            jsonArray.forEach { jsonElement ->
                jsonElement.asJsonObject.let {
                    val carModel = CarModel(
                        it.get("id").asInt,
                        it.get("brand").asString,
                        it.get("model").asString,
                        FuelType.fromString(it.get("fuel").asString),
                        it.get("options").asString,
                        it.get("price").asFloat,
                        it.get("licensePlate").asString,
                        it.get("engineSize").asInt,
                        it.get("nrOfSeats").asInt,
                        it.get("modelYear").asInt,
                        it.get("since").asString,
                        Body.fromString(it.get("body").asString)
                    )
                    carRepository.addCar(carModel)
                    Log.i("CHECK_RESPONSE", "Car added: $carModel")
                }
            }
        }
    }
}