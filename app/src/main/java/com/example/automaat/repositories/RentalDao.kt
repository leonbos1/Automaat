package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

@Dao
interface RentalDao {
    @Query("SELECT * FROM rentals ORDER BY id ASC")
    fun readAllData(): LiveData<List<RentalModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRental(rental: RentalModel)

    @Query("DELETE FROM rentals")
    suspend fun deleteAllRentals()

    @Transaction
    @Query("SELECT * FROM rentals" +
            " LEFT JOIN cars ON rentals.carId = cars.id" +
            " LEFT JOIN customers ON rentals.customerId = customers.id" +
            " WHERE rentals.customerId = :customerId")
    fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>>
}