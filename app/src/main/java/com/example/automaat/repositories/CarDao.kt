package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY id ASC")
    fun readAllData(): LiveData<List<CarModel>>

    @Transaction
    @Query("SELECT * FROM Cars")
    fun getCarsWithRentals(): LiveData<List<CarWithRental>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCar(car: CarModel)

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()
}