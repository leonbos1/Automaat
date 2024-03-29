package com.example.automaat.ui.home

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.syncers.CarSyncManager
import com.example.automaat.api.syncers.CustomerSyncManager
import com.example.automaat.api.syncers.InspectionSyncManager
import com.example.automaat.api.syncers.RentalSyncManager
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.CustomerRepository
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import com.example.automaat.utils.NetworkMonitor
import com.example.automaat.utils.SnackbarManager
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val carRepository: CarRepository
    private val rentalRepository: RentalRepository
    private val customerRepository: CustomerRepository
    private val inspectionRepository: InspectionRepository

    val readAllData: LiveData<List<CarModel>>
    val carsWithRentals: LiveData<List<CarWithRental>>

    private var carsSyncManager: CarSyncManager
    private var rentalSyncManager: RentalSyncManager
    private var customerSyncManager: CustomerSyncManager
    private var inspectionSyncManager: InspectionSyncManager

    init {
        val database = AutomaatDatabase.getDatabase(application)
        carRepository = CarRepository(database.carDao())
        rentalRepository = RentalRepository(database.rentalDao())
        customerRepository = CustomerRepository(database.customerDao())
        inspectionRepository = InspectionRepository(database.inspectionDao())

        readAllData = carRepository.readAllData
        carsWithRentals = carRepository.getCarsWithRentals()

        carsSyncManager = CarSyncManager(carRepository)
        rentalSyncManager = RentalSyncManager(rentalRepository, application)
        customerSyncManager = CustomerSyncManager(customerRepository)
        inspectionSyncManager = InspectionSyncManager(inspectionRepository)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun refreshCars(context: Context) {
        viewModelScope.launch {
            if (NetworkMonitor.isConnected) {
                try {
                    carsSyncManager.syncEntities(context)
                    rentalSyncManager.syncEntities(context)
                    customerSyncManager.syncEntities(context)
                    inspectionSyncManager.syncEntities(context)
                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error while syncing", e)
                }
            } else {
                SnackbarManager.showErrorSnackbar(context, "Can't sync when there is no internet connection...")
            }
        }
    }
}
