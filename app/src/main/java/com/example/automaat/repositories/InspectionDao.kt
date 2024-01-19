package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.relations.InspectionWithCarWithRental

@Dao
interface InspectionDao {
    @Query("SELECT * FROM inspections ORDER BY id ASC")
    fun readAllData(): LiveData<List<InspectionModel>>

    @Query("SELECT * FROM inspections ORDER BY id ASC")
    suspend fun getAll(): List<InspectionModel>

    @Query("INSERT INTO inspections (id, rentalId, carId, odometer, result, photo, photoContentType, completed, photos, employeeId) VALUES (:id, :rentalId, :carId, :odometer, :result, :photo, :photoContentType, :completed, :photos, :employeeId)")
    suspend fun createNewInspection(
        id: Int,
        rentalId: Int,
        carId: Int,
        odometer: Int,
        result: String,
        photo: String,
        photoContentType: String,
        completed: String,
        photos: String,
        employeeId: Int
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(inspection: InspectionModel) {
        println("THIS IS THE INSPECTION: ${inspection.photo}")
        println("THIS IS THE INSPECTION: ${inspection.result}")
        println("THIS IS THE INSPECTION: ${inspection.id}")
    }

    @Query("SELECT * FROM inspections WHERE id = :id")
    suspend fun getInspectionById(id: Int): InspectionModel

    @Query("DELETE FROM inspections")
    suspend fun deleteAllInspections()

    //doesnt work for some reason

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInspection(inspection: InspectionModel)

    @Transaction
    @Query(
        "SELECT * FROM inspections" +
                " LEFT JOIN rentals ON inspections.rentalId = rentals.id" +
                " LEFT JOIN cars ON inspections.carId = cars.id" +
                " WHERE inspections.carId = :carId"
    )
    suspend fun getInspectionWithCarWithRentalByCarId(carId: Int): InspectionWithCarWithRental
}