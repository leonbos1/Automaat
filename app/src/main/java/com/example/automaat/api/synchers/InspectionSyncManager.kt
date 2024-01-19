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
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InspectionSyncManager(private val inspectionRepository: InspectionRepository) : ISyncManager {
    override fun syncEntities() {
        Authentication().authenticate {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    syncRemoteInspectionsToLocal()
                    syncLocalInspectionsToServer()
                } catch (e: Exception) {
                    Log.e("CHECK_RESPONSE", "Error while syncing inspections", e)
                }
            }
        }
    }

    private fun syncLocalInspectionsToServer() {

    }

    private suspend fun syncRemoteInspectionsToLocal() {
        val remoteInspections = Inspections().getAllInspections() ?: return

        for (inspection in remoteInspections) {
            val remoteInspection = parseJsonToInspectionModel(inspection as JsonObject)

            println("ADDING INSPECTION WITH ID: ${remoteInspection.id} TO RESULT: ${remoteInspection.result} AND rentalid: ${remoteInspection.rentalId}")

            inspectionRepository.insertInspection(remoteInspection)
        }
    }

    private fun parseJsonToInspectionModel(json: JsonObject): InspectionModel {
        val carObj = json.get("car")?.takeIf { it.isJsonObject }?.asJsonObject
        val carId = carObj?.get("id")?.asInt
        val rentalObj = json.get("rental")?.takeIf { it.isJsonObject }?.asJsonObject
        val rentalId = rentalObj?.get("id")?.asInt

        return InspectionModel(
            json.get("id").asInt,
            json.get("code").asString,
            json.get("odometer").asInt,
            json.get("result").asString,
            json.get("photo").asString,
            json.get("photoContentType").asString,
            json.get("completed").asString,
            null,
            carId,
            null,
            rentalId
        )
    }
}
