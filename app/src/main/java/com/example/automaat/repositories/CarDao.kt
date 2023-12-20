package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.automaat.models.car.CarModel

@Dao
interface CarDao {
    @Query("SELECT * FROM cars ORDER BY id ASC")
    fun readAllData(): LiveData<List<CarModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCar(car: CarModel)
}