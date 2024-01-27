package com.example.automaat

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.entities.InspectionModel
import com.example.automaat.repositories.CarDao
import com.example.automaat.repositories.InspectionDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class InspectionUnitTest {
    private lateinit var inspectionDao: InspectionDao
    private lateinit var db: AutomaatDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AutomaatDatabase::class.java
        ).build()
        inspectionDao = db.inspectionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testInsertAndGetInspection() {
        val inspection = InspectionModel(
            id = 1,
            code = "ABC123",
            odometer = 1000,
            result = "Pass",
            photo = "photo_url",
            photoContentType = "image/jpeg",
            completed = "2022-01-30",
            photos = "photo1,photo2",
            carId = 1,
            employeeId = 2,
            rentalId = 3
        )

        runBlocking {
            inspectionDao.insertInspection(inspection)
            val retrievedInspection = inspectionDao.getInspectionByIdAsync(1)

            assertNotNull(retrievedInspection)
            assertEquals(1, retrievedInspection.id)
            assertEquals("ABC123", retrievedInspection.code)
            assertEquals(1000, retrievedInspection.odometer)
            assertEquals("Pass", retrievedInspection.result)
            assertEquals("photo_url", retrievedInspection.photo)
            assertEquals("image/jpeg", retrievedInspection.photoContentType)
            assertEquals("2022-01-30", retrievedInspection.completed)
            assertEquals("photo1,photo2", retrievedInspection.photos)
            assertEquals(1, retrievedInspection.carId)
            assertEquals(2, retrievedInspection.employeeId)
            assertEquals(3, retrievedInspection.rentalId)
        }
    }

    @Test
    fun testUpdateInspection() {
        val inspection = InspectionModel(
            id = 1,
            code = "ABC123",
            odometer = 1000,
            result = "Pass",
            photo = "photo_url",
            photoContentType = "image/jpeg",
            completed = "2022-01-30",
            photos = "photo1,photo2",
            carId = 1,
            employeeId = 2,
            rentalId = 3
        )

        runBlocking {
            inspectionDao.insertInspection(inspection)

            val updatedInspection = inspection.copy(
                result = "Fail",
                photo = "updated_photo_url"
            )

            inspectionDao.updateInspection(updatedInspection)

            val retrievedInspection = inspectionDao.getInspectionByIdAsync(1)

            assertNotNull(retrievedInspection)
            assertEquals("Fail", retrievedInspection.result)
            assertEquals("updated_photo_url", retrievedInspection.photo)
        }
    }

    @Test
    fun testDeleteInspection() {
        val inspection = InspectionModel(
            id = 1,
            code = "ABC123",
            odometer = 1000,
            result = "Pass",
            photo = "photo_url",
            photoContentType = "image/jpeg",
            completed = "2022-01-30",
            photos = "photo1,photo2",
            carId = 1,
            employeeId = 2,
            rentalId = 3
        )

        runBlocking {
            inspectionDao.insertInspection(inspection)

            inspectionDao.deleteInspection(inspection.id)

            val retrievedInspection = inspectionDao.getInspectionByIdAsync(1)

            assertNull(retrievedInspection)
        }
    }
}
