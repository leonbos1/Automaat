package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.automaat.models.car.CarModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarRepository(private val carDao: CarDao) {
    val readAllData: LiveData<List<CarModel>> = carDao.readAllData()

    suspend fun addCar(car: CarModel) {
        carDao.addCar(car)
    }

    fun insertDummyCars() {
        val car1 = CarModel(getRandomId(), "BMW", "X5", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0)
        val car2 = CarModel(getRandomId(), "BMW", "M3", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0)
        val car3 =
            CarModel(getRandomId(), "Volkswagen", "Golf GTI", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0)
        val car4 =
            CarModel(getRandomId(), "Alfa Romeo", "Guilia", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0)
        val car5 = CarModel(getRandomId(), "Toyota", "Prius", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0)

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            addCar(car1)
            addCar(car2)
            addCar(car3)
            addCar(car4)
            addCar(car5)
        }
    }

    fun getRandomId(): Int {
        return (0..1000000).random()
    }

    fun getModelsByBrand(brand: String): List<String> {
        val cars = readAllData.value
        val models = cars?.filter { it.brand == brand }
            ?.mapNotNull { it.model }
            ?.distinct()

        return models ?: emptyList()
    }
}