package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.relations.InspectionWithCarWithRental

class InspectionRepository(private val inspectionDao: InspectionDao) {
    val readAllData: LiveData<List<InspectionModel>> = inspectionDao.readAllData()

    suspend fun insertInspection(inspection: InspectionModel) {
        inspectionDao.insertInspection(inspection)
    }

    suspend fun createNewInspection(inspectionId:Int, rentalId: Int, carId: Int) {
        inspectionDao.createNewInspection(
            inspectionId,
            rentalId,
            carId,
            0,
            "",
            "",
            "",
            "",
            "",
            0
        )
    }

    suspend fun deleteAllInspections() {
        inspectionDao.deleteAllInspections()
    }

    suspend fun updateInspection(inspection: InspectionModel) {
        println("UPDATING INSPECTION WITH ID: ${inspection.id} TO RESULT: ${inspection.result} AND PHOTO: ${inspection.photo}")
        inspectionDao.updateInspection(inspection)
    }

    suspend fun getAll(): List<InspectionModel> {
        return inspectionDao.getAll()
    }

    suspend fun getInspectionById(id: Int): InspectionModel {
        return inspectionDao.getInspectionById(id)
    }

    suspend fun getInspectionWithCarWithRentalByCarId(carId: Int): InspectionWithCarWithRental {
        return inspectionDao.getInspectionWithCarWithRentalByCarId(carId)
    }
}