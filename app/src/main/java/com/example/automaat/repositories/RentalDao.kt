package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.automaat.entities.RentalModel

@Dao
interface RentalDao {
    @Query("SELECT * FROM rentals ORDER BY id ASC")
    fun readAllData(): LiveData<List<RentalModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRental(rental: RentalModel)

    @Query("DELETE FROM rentals")
    suspend fun deleteAllRentals()
}