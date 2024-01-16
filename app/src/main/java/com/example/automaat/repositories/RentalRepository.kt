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

    suspend fun getAll(): List<RentalModel> {
        return rentalDao.getAll()
    }

    suspend fun getRentalById(id: Int): RentalModel {
        return rentalDao.getRentalById(id)
    }

    suspend fun getByCarId(carId: Int): List<RentalModel> {
        return rentalDao.getByCarId(carId)
    }

    suspend fun updateRental(rental: RentalModel) {
        println("updateRental")
        rental.customerId?.let {
            rental.carId?.let { it1 ->
                rentalDao.updateRental(rental.id, rental.fromDate, rental.toDate, rental.state,
                    it, it1
                )
            }
        }
    }

    suspend fun insertRental(rental: RentalModel) {
        rentalDao.insertRental(rental)

        //sync with server

    }

    suspend fun createRental(rental: RentalModel) {
        rentalDao.insertRental(rental)

        //sync with server

    }

    fun getRentalsWithCarAndCustomerByCustomer(customerId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByCustomer(customerId)
    }

    fun getRentalsWithCarAndCustomerByRental(rentalId: Int): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalDao.getRentalsWithCarAndCustomerByRental(rentalId)
    }

    fun getRentalsWithCarAndCustomerByRentalAsync(rentalId: Int): RentalWithCarWithCustomer {
        return rentalDao.getRentalsWithCarAndCustomerByRentalAsync(rentalId)
    }
}