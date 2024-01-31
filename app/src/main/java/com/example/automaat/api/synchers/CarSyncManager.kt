package com.example.automaat.api.synchers

import android.content.Context
import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Cars
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.repositories.CarRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarSyncManager(private val carRepository: CarRepository) : ISyncManager {
    override fun syncEntities(context: Context) {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {
                val jsonArray = Cars().getAllCars()
                //TODO dont make new instance of Cars() here, use the one from the constructor
                if (jsonArray != null) {
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
                            try {
                                carRepository.addCar(carModel)
                            } catch (e: Exception) {
                                Log.e("CHECK_RESPONSE", "Error while adding car: $carModel", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
