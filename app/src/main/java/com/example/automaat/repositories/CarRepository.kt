package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.FuelType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CarRepository(private val carDao: CarDao) {
    val readAllData: LiveData<List<CarModel>> = carDao.readAllData()

    fun getCarsWithRentals(): LiveData<List<CarWithRental>> {
        return carDao.getCarsWithRentals()
    }

    suspend fun addCar(car: CarModel) {
        carDao.addCar(car)
    }

    suspend fun deleteAllCars() {
        carDao.deleteAllCars()
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

    fun insertDummyCars() {
        val car1 = CarModel(
            getRandomId(),
            brand = "BMW",
            model = "X5",
            fuelType = FuelType.GASOLINE,
            options = "Navigation",
            price = 150f,
            licensePlate = "05-TGB-10",
            engineSize = 3000,
            numOfSeats = 5,
            modelYear = 2023,
            since = "2023-12-21",
            body = Body.SUV
        )

        val car2 = CarModel(
            getRandomId(),
            brand = "BMW",
            model = "M3",
            fuelType = FuelType.GASOLINE,
            options = "Sunroof",
            price = 80f,
            licensePlate = "05-ABC-20",
            engineSize = 3000,
            numOfSeats = 4,
            modelYear = 2023,
            since = "2023-12-22",
            body = Body.SEDAN
        )

        val car3 = CarModel(
            getRandomId(),
            brand = "Alfa Romeo",
            model = "Guilia",
            fuelType = FuelType.GASOLINE,
            options = "Sports Package",
            price = 175f,
            licensePlate = "05-XYZ-30",
            engineSize = 2900,
            numOfSeats = 4,
            modelYear = 2023,
            since = "2023-12-23",
            body = Body.SEDAN
        )

        val car4 = CarModel(
            getRandomId(),
            brand = "Volkswagen",
            model = "Golf GTI",
            fuelType = FuelType.GASOLINE,
            options = "Performance Package",
            price = 95f,
            licensePlate = "05-PQR-40",
            engineSize = 3000,
            numOfSeats = 4,
            modelYear = 2023,
            since = "2023-12-24",
            body = Body.HATCHBACK
        )

        val car5 = CarModel(
            getRandomId(),
            brand = "Toyota",
            model = "Prius",
            fuelType = FuelType.HYBRID,
            options = "Technology Package",
            price = 30f,
            licensePlate = "05-LMN-50",
            engineSize = 3000,
            numOfSeats = 4,
            modelYear = 2023,
            since = "2023-12-25",
            body = Body.SEDAN
        )

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            addCar(car1)
            addCar(car2)
            addCar(car3)
            addCar(car4)
            addCar(car5)
        }
    }
}