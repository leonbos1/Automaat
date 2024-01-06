package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.relations.CarWithRental

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY id ASC")
    fun readAllData(): LiveData<List<CustomerModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCustomer(customer: CustomerModel)

    @Query("DELETE FROM customers")
    suspend fun deleteAllCustomers()
}