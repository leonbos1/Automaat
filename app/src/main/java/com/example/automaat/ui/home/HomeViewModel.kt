package com.example.automaat.ui.home

import android.app.Application
import android.os.Debug
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.endpoints.Cars
import com.example.automaat.api.synchers.CarSyncManager
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.repositories.CarRepository
import kotlinx.coroutines.launch
import java.io.Console

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<CarModel>>
    private val carRepository: CarRepository
    lateinit var carsWithRentals: LiveData<List<CarWithRental>>
    lateinit var carsSynchManager: CarSyncManager

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        carRepository = CarRepository(carDao)
        readAllData = carRepository.readAllData
        carsSynchManager = CarSyncManager(carRepository)

        viewModelScope.launch {
            carsWithRentals = carRepository.getCarsWithRentals()
        }
    }

    fun refreshCars() {
        viewModelScope.launch {
            carsSynchManager.syncEntities()
        }
    }
}