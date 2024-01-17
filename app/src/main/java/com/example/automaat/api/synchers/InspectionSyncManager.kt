package com.example.automaat.api.synchers

import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Cars
import com.example.automaat.api.endpoints.Inspections
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.entities.InspectionModel
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.InspectionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InspectionSyncManager(private val inspectionRepository: InspectionRepository) : ISyncManager {
    override fun syncEntities() {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {
                val jsonArray = Inspections().getAllInspections()

                if (jsonArray != null) {
                    jsonArray.forEach { jsonElement ->
                        jsonElement.asJsonObject.let {
                            try {
                                val carObj = it.get("car")?.takeIf { it.isJsonObject }?.asJsonObject
                                val carId = carObj?.get("id")?.asInt
                                val rentalObj =
                                    it.get("rental")?.takeIf { it.isJsonObject }?.asJsonObject
                                val rentalId = rentalObj?.get("id")?.asInt

                                val inspectionModel = InspectionModel(
                                    it.get("id").asInt,
                                    it.get("code").asString,
                                    it.get("odometer").asInt,
                                    it.get("result").asString,
                                    it.get("photo").asString,
                                    it.get("photoContentType").asString,
                                    it.get("completed").asString,
                                    null,
                                    carId,
                                    null,
                                    rentalId
                                )
                                try {
                                    Log.d("CHECK_RESPONSE", "Adding inspection: $inspectionModel")
                                    inspectionRepository.addInspection(inspectionModel)
                                } catch (e: Exception) {
                                    Log.e(
                                        "CHECK_RESPONSE",
                                        "Error while adding inspection: $inspectionModel",
                                        e
                                    )
                                }
                            } catch (e: Exception) {
                                Log.e("CHECK_RESPONSE", "Error while adding inspection: $it", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
