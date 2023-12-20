package com.example.automaat.repositories

import androidx.lifecycle.LiveData
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

    fun getAvailableModelsByBrand(brand: String): ArrayList<String> {
        val availableModels = arrayListOf<String>()
        val allCars = readAllData.value

        if (allCars != null) {
            for (car in allCars) {
                if (car.brand == brand) {
                    var model = car.model
                    if (model != null) {
                        availableModels.add(model)
                    }
                }
            }
        }

        return availableModels
    }

    fun getAvailableBrands(): ArrayList<String> {
        val availableBrands = mutableListOf<String>()
        val allCars = readAllData.value

        if (allCars != null) {
            for (car in allCars) {
                val brand = car.brand
                if (brand != null) {
                    availableBrands.add(brand)
                }
            }
        }

        return ArrayList(availableBrands.distinct())
    }

    fun getAvailableModels(): ArrayList<String> {
        val availableModels = mutableListOf<String>()
        val allCars = readAllData.value

        if (allCars != null) {
            for (car in allCars) {
                var model = car.model
                if (model != null) {
                    availableModels.add(model)
                }
            }
        }

        return ArrayList(availableModels.distinct())
    }
}