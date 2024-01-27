package com.example.automaat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.repositories.CarDao
import com.example.automaat.repositories.RentalDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RentalUnitTest {
    private lateinit var rentalDao: RentalDao
    private lateinit var db: AutomaatDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AutomaatDatabase::class.java
        ).build()
        rentalDao = db.rentalDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeRentalAndReadInList() = runBlocking {
        val rental = RentalModel(
            1,
            "",
            0.0f,
            0.0f,
            "01-01-2000",
            "01-01-2000",
            RentalState.RETURNED,
            0,
            1,
            1,
        )

        rentalDao.addRental(rental)

        val rentalList = rentalDao.readAllDataAsync()

        assertEquals(rentalList[0].id, rental.id)
        assertEquals(rentalList[0].carId, rental.carId)
        assertEquals(rentalList[0].customerId, rental.customerId)
    }

    //test getRentalsWithCarAndCustomerByRentalAsync
    @Test
    @Throws(Exception::class)
    fun getRentalsWithCarAndCustomerByRentalAsync() = runBlocking {
        val rental = RentalModel(
            1,
            "",
            0.0f,
            0.0f,
            "01-01-2000",
            "01-01-2000",
            RentalState.RETURNED,
            0,
            1,
            1,
        )

        rentalDao.addRental(rental)

        val rentalList = rentalDao.getRentalsWithCarAndCustomerByRentalAsync(1)

        val rentalWithCarWithCustomer = rentalList[0]

        assertEquals(rentalWithCarWithCustomer.rental?.id, rental.id)
        assertEquals(rentalWithCarWithCustomer.rental?.carId, rental.carId)
        assertEquals(rentalWithCarWithCustomer.rental?.customerId, rental.customerId)
    }

    @Test
    @Throws(Exception::class)
    fun getRentalsWithCarAndCustomerByCarId() = runBlocking {
        val rental = RentalModel(
            1,
            "",
            0.0f,
            0.0f,
            "01-01-2000",
            "01-01-2000",
            RentalState.RETURNED,
            0,
            1,
            1,
        )

        rentalDao.addRental(rental)

        val rentalList = rentalDao.getRentalsWithCarAndCustomerByCarId(1)

        val rentalWithCarWithCustomer = rentalList[0]

        assertEquals(rentalWithCarWithCustomer.rental?.id, rental.id)
        assertEquals(rentalWithCarWithCustomer.rental?.carId, rental.carId)
        assertEquals(rentalWithCarWithCustomer.rental?.customerId, rental.customerId)
    }

    @Test
    @Throws(Exception::class)
    fun updateRental() = runBlocking {
        val rental = RentalModel(
            1,
            "",
            0.0f,
            0.0f,
            "01-01-2000",
            "01-01-2000",
            RentalState.RETURNED,
            0,
            1,
            1,
        )

        rentalDao.addRental(rental)

        val rentalList = rentalDao.readAllDataAsync()

        assertEquals(rentalList[0].id, rental.id)
        assertEquals(rentalList[0].carId, rental.carId)
        assertEquals(rentalList[0].customerId, rental.customerId)

        rentalDao.updateRental(1, "01-01-2000", "01-01-2000", RentalState.RETURNED, 1, 1, 1)

        val rentalList2 = rentalDao.readAllDataAsync()

        assertEquals(rentalList2[0].id, rental.id)
        assertEquals(rentalList2[0].carId, rental.carId)
        assertEquals(rentalList2[0].customerId, rental.customerId)
    }
}
