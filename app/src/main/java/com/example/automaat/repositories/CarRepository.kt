package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.models.car.CarModel

class CarRepository(private val carDao: CarDao) {
    val readAllData: LiveData<List<CarModel>> = carDao.readAllData()
}