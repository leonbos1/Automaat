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
    suspend fun insertInspection(inspection: InspectionModel)

    @Query("SELECT * FROM inspections WHERE rentalId = :rentalId")
    fun getInspectionByRentalId(rentalId: Int): LiveData<InspectionModel>

    @Query("SELECT * FROM inspections WHERE id = :id")
    fun getInspectionById(id: Int): LiveData<InspectionModel>

    @Query("SELECT * FROM inspections WHERE id = :id")
    fun getInspectionByIdAsync(id: Int): InspectionModel

    @Query("DELETE FROM inspections")
    suspend fun deleteAllInspections()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInspection(inspection: InspectionModel)

    @Query("DELETE FROM inspections WHERE id = :id")
    suspend fun deleteInspection(id: Int)

    @Transaction
    @Query(
        "SELECT * FROM inspections" +
                " LEFT JOIN rentals ON inspections.rentalId = rentals.id" +
                " LEFT JOIN cars ON inspections.carId = cars.id" +
                " WHERE inspections.carId = :carId"
    )
    fun getInspectionWithCarWithRentalByCarId(carId: Int): LiveData<InspectionWithCarWithRental>
}