package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.RentalModel

class RentalRepository(private val rentalDao: RentalDao) {
    val readAllData: LiveData<List<RentalModel>> = rentalDao.readAllData()

    suspend fun addRental(rental: RentalModel) {
        rentalDao.addRental(rental)
    }

    suspend fun deleteAllRentals() {
        rentalDao.deleteAllRentals()
    }
}