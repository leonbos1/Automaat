package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.automaat.models.car.CarModel

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAll(): List<CarModel>

    @Query("SELECT * FROM cars")
    fun readAllData(): LiveData<List<CarModel>>
}