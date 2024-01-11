package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

@Dao
interface RentalDao {
    @Query("SELECT * FROM rentals ORDER BY id ASC")
    fun readAllData(): LiveData<List<RentalModel>>

    @Query("SELECT * FROM rentals ORDER BY id ASC")
    suspend fun getAll(): List<RentalModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRental(rental: RentalModel)

    @Query("SELECT * FROM rentals WHERE id = :id")
    suspend fun getRentalById(id: Int): RentalModel

    @Query("DELETE FROM rentals")
    suspend fun deleteAllRentals()

    @Query("UPDATE rentals SET fromDate = :fromDate, toDate = :toDate, state = :state, customerId = :customerId, carId = :carId WHERE id = :id")
    suspend fun updateRental(id: Int, fromDate: String, toDate: String, state: RentalState, customerId: Int, carId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRental(rental: RentalModel)

    @Transaction
    @Query("SELECT * FROM rentals" +
            " LEFT JOIN cars ON rentals.carId = cars.id" +
            " LEFT JOIN customers ON rentals.customerId = customers.id" +
            " WHERE rentals.customerId = :customerId")
    fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>>

    @Transaction
    @Query("SELECT * FROM rentals" +
            " LEFT JOIN cars ON rentals.carId = cars.id" +
            " LEFT JOIN customers ON rentals.customerId = customers.id" +
            " WHERE rentals.id = :rentalId")
    fun getRentalsWithCarAndCustomerByRental(rentalId: Int): LiveData<List<RentalWithCarWithCustomer>>

    @Query("SELECT * FROM rentals" +
            " LEFT JOIN cars ON rentals.carId = cars.id" +
            " LEFT JOIN customers ON rentals.customerId = customers.id" +
            " WHERE rentals.id = :rentalId")
    fun getRentalsWithCarAndCustomerByRentalAsync(rentalId: Int): RentalWithCarWithCustomer
}