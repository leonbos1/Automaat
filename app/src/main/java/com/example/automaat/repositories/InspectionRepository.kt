package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.relations.InspectionWithCarWithRental

class InspectionRepository(private val inspectionDao: InspectionDao) {
    val readAllData: LiveData<List<InspectionModel>> = inspectionDao.readAllData()

    suspend fun addInspection(inspection: InspectionModel) {
        inspectionDao.addInspection(inspection)
    }

    suspend fun deleteAllInspections() {
        inspectionDao.deleteAllInspections()
    }

    suspend fun getAll(): List<InspectionModel> {
        return inspectionDao.getAll()
    }

    suspend fun getInspectionById(id: Int): InspectionModel {
        return inspectionDao.getInspectionById(id)
    }

    suspend fun getInspectionWithCarWithRentalByRentalId(rentalId: Int): InspectionWithCarWithRental {
        return inspectionDao.getInspectionWithCarWithRentalByRentalId(rentalId)
    }
}