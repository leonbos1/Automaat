package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

class RentalRepository(private val rentalDao: RentalDao) {
    val readAllData: LiveData<List<RentalModel>> = rentalDao.readAllData()

    suspend fun addRental(rental: RentalModel) {
        rentalDao.addRental(rental)
    }

    suspend fun deleteAllRentals() {
        rentalDao.deleteAllRentals()
    }

    fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByCustomer(customerId)
    }
}