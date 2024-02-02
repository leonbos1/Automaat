package com.example.automaat.api.syncers

import android.content.Context
import android.util.Log
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Inspections
import com.example.automaat.entities.InspectionModel
import com.example.automaat.repositories.InspectionRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InspectionSyncManager(private val inspectionRepository: InspectionRepository) : ISyncManager {
    override fun syncEntities(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    syncRemoteInspectionsToLocal(context)
                    syncLocalInspectionsToServer(context)
                } catch (e: Exception) {
                    Log.e("CHECK_RESPONSE", "Error while syncing inspections", e)
                }
        }
    }

    private suspend fun syncLocalInspectionsToServer(context: Context) {
        val localInspections = inspectionRepository.getAll()

        for (inspection in localInspections) {
            try {
                val remoteInspection = parseInspectionModelToJson(inspection)
                Inspections().updateInspection(remoteInspection, context)
            } catch (e: Exception) {
                Log.e("CHECK_RESPONSE", "Error while syncing inspection", e)
            }
        }
    }

    private suspend fun syncRemoteInspectionsToLocal(context: Context) {
        val remoteInspections = Inspections().getAllInspections(context) ?: return

        for (inspection in remoteInspections) {
            val remoteInspection = parseJsonToInspectionModel(inspection as JsonObject)
            val inspectionId = remoteInspection.id
            val localInspection = inspectionRepository.getInspectionByIdAsync(inspectionId)

            if (localInspection == null || (localInspection.result == "" && localInspection.photo == "")) {
                inspectionRepository.insertInspection(remoteInspection)
            }
        }
    }

    private fun parseJsonToInspectionModel(json: JsonObject): InspectionModel {
        val carObj = json.get("car")?.takeIf { it.isJsonObject }?.asJsonObject
        val carId = carObj?.get("id")?.asInt
        val rentalObj = json.get("rental")?.takeIf { it.isJsonObject }?.asJsonObject
        val rentalId = rentalObj?.get("id")?.asInt

        val id = json.get("id")?.takeIf { it.isJsonPrimitive }?.asInt ?: 0
        val code = json.get("code")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val odometer = json.get("odometer")?.takeIf { it.isJsonPrimitive }?.asInt ?: 0
        val result = json.get("result")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val photo = json.get("photo")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val photoContentType =
            json.get("photoContentType")?.takeIf { it.isJsonPrimitive }?.asString ?: ""
        val completed = json.get("completed")?.takeIf { it.isJsonPrimitive }?.asString ?: ""

        return InspectionModel(
            id,
            code,
            odometer,
            result,
            photo,
            photoContentType,
            completed,
            null,
            carId,
            null,
            rentalId
        )
    }

    private fun parseInspectionModelToJson(inspection: InspectionModel): JsonObject {
        val carObj = JsonObject()
        carObj.addProperty("id", inspection.carId)

        val rentalObj = JsonObject()
        rentalObj.addProperty("id", inspection.rentalId)

        val inspectionObj = JsonObject()
        inspectionObj.addProperty("id", inspection.id)
        inspectionObj.addProperty("code", inspection.code)
        inspectionObj.addProperty("odometer", inspection.odometer)
        inspectionObj.addProperty("result", inspection.result)
        inspectionObj.addProperty("photo", inspection.photo)
        inspectionObj.addProperty("photoContentType", inspection.photoContentType)
        inspectionObj.addProperty("completed", inspection.completed)
        inspectionObj.add("car", carObj)
        inspectionObj.add("rental", rentalObj)

        return inspectionObj
    }
}
