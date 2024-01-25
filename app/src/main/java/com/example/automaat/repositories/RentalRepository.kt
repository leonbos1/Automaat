package com.example.automaat.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    suspend fun getAll(): List<RentalModel> {
        return rentalDao.getAll()
    }

    suspend fun getRentalById(id: Int): RentalModel {
        return rentalDao.getRentalById(id)
    }

    suspend fun getByCarId(carId: Int?): List<RentalModel> {
        if (carId == null) {
            return emptyList()
        }
        return rentalDao.getByCarId(carId)
    }

    suspend fun updateRental(rental: RentalModel) {
        rentalDao.updateRental(
            rental.id, rental.fromDate, rental.toDate, rental.state,
            rental.customerId, rental.carId, rental.inspectionId
        )
    }

    suspend fun insertRental(rental: RentalModel) {
        rentalDao.insertRental(rental)
    }

    suspend fun createRental(rental: RentalModel) {
        rentalDao.insertRental(rental)
    }

    suspend fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByCustomer(customerId)
    }

    suspend fun getRentalsWithCarAndCustomerByRental(rentalId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByRental(rentalId)
    }

    suspend fun getRentalsWithCarAndCustomerByRentalAsync(rentalId: Int): RentalWithCarWithCustomer {
        return rentalDao.getRentalsWithCarAndCustomerByRentalAsync(rentalId)
    }
}