package com.example.automaat.ui.home

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.synchers.CarSyncManager
import com.example.automaat.api.synchers.CustomerSyncManager
import com.example.automaat.api.synchers.RentalSyncManager
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.CustomerRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val carRepository: CarRepository
    private val rentalRepository: RentalRepository
    private val customerRepository: CustomerRepository

    val readAllData: LiveData<List<CarModel>>
    val carsWithRentals: LiveData<List<CarWithRental>>

    private var carsSyncManager: CarSyncManager
    private var rentalSyncManager: RentalSyncManager
    private var customerSyncManager: CustomerSyncManager

    init {
        val database = AutomaatDatabase.getDatabase(application)
        carRepository = CarRepository(database.carDao())
        rentalRepository = RentalRepository(database.rentalDao())
        customerRepository = CustomerRepository(database.customerDao())

        readAllData = carRepository.readAllData
        carsWithRentals = carRepository.getCarsWithRentals()

        carsSyncManager = CarSyncManager(carRepository)
        rentalSyncManager = RentalSyncManager(rentalRepository, application)
        customerSyncManager = CustomerSyncManager(customerRepository)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun refreshCars() {
        viewModelScope.launch {
            try {
                carsSyncManager.syncEntities()
                rentalSyncManager.syncEntities()
                customerSyncManager.syncEntities()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error while syncing", e)
            }
        }
    }
}
