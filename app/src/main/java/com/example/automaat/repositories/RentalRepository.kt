package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

class RentalRepository(private val rentalDao: RentalDao) {
    val readAllData: LiveData<List<RentalModel>> = rentalDao.readAllData()

    suspend fun addRental(rental: RentalModel) {
        rentalDao.addRental(rental)
    }

    suspend fun deleteAllRentals() {
        rentalDao.deleteAllRentals()
    }

    suspend fun updateRental(rental: RentalModel) {
        rentalDao.updateRental(rental.id, rental.fromDate, rental.toDate, rental.state)
    }

    suspend fun insertRental(rental: RentalModel) {
        rentalDao.insertRental(rental)

        //sync with server

    }

    fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByCustomer(customerId)
    }

    fun getRentalsWithCarAndCustomerByRental(rentalId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByRental(rentalId)
    }
}