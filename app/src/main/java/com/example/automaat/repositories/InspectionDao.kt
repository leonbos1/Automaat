package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.relations.InspectionWithCarWithRental

@Dao
interface InspectionDao {
    @Query("SELECT * FROM inspections ORDER BY id ASC")
    fun readAllData(): LiveData<List<InspectionModel>>

    @Query("SELECT * FROM inspections ORDER BY id ASC")
    suspend fun getAll(): List<InspectionModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInspection(inspection: InspectionModel)

    @Query("SELECT * FROM inspections WHERE id = :id")
    suspend fun getInspectionById(id: Int): InspectionModel

    @Query("DELETE FROM inspections")
    suspend fun deleteAllInspections()

    //getInspectionWithCustomerWithRentalByRentalId
    @Transaction
    @Query("SELECT * FROM inspections" +
            " LEFT JOIN rentals ON inspections.rentalId = rentals.id" +
            " LEFT JOIN cars ON inspections.carId = cars.id" +
            " WHERE inspections.rentalId = :rentalId")
    fun getInspectionWithCarWithRentalByRentalId(rentalId: Int): InspectionWithCarWithRental
}