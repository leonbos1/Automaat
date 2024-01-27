package com.example.automaat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.repositories.CarDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CarUnitTest {
    private lateinit var carDao: CarDao
    private lateinit var db: AutomaatDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AutomaatDatabase::class.java
        ).build()
        carDao = db.carDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCarAndReadInList() = runBlocking {
        val car = CarModel(
            1,
            "Audi",
            "A4",
            FuelType.DIESEL,
            "options",
            100.0f,
            "licensePlate",
            2000,
            5,
            2021,
            "since",
            Body.SEDAN
        )

        carDao.addCar(car)

        val carList = carDao.readAllDataAsync()

        assertEquals(carList[0].brand, "Audi")
        assertEquals(carList[0].model, "A4")
        assertEquals(carList[0].fuelType, FuelType.DIESEL)
    }

    @Test
    fun deleteCar() = runBlocking {
        val car = CarModel(
            1,
            "Audi",
            "A4",
            FuelType.DIESEL,
            "options",
            100.0f,
            "licensePlate",
            2000,
            5,
            2021,
            "since",
            Body.SEDAN
        )

        carDao.addCar(car)

        carDao.deleteCarAsync(car.id)

        val retrievedCar = carDao.getCarByIdAsync(1)

        assertNull(retrievedCar)
    }

    @Test
    fun deleteAllCars() = runBlocking {
        val car = CarModel(
            1,
            "Audi",
            "A4",
            FuelType.DIESEL,
            "options",
            100.0f,
            "licensePlate",
            2000,
            5,
            2021,
            "since",
            Body.SEDAN
        )

        carDao.addCar(car)

        val car2 = CarModel(
            2,
            "Audi",
            "A4",
            FuelType.DIESEL,
            "options",
            100.0f,
            "licensePlate",
            2000,
            5,
            2021,
            "since",
            Body.SEDAN
        )

        carDao.addCar(car2)

        carDao.deleteAllCars()

        val carList = carDao.readAllDataAsync()

        assertEquals(carList.size, 0)
    }
}
