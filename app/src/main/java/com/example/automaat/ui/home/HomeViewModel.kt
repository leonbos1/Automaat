package com.example.automaat.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.repositories.CarRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<CarModel>>
    private val repository: CarRepository
    lateinit var carsWithRentals: LiveData<List<CarWithRental>>

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        repository = CarRepository(carDao)
        readAllData = repository.readAllData

        viewModelScope.launch {
            carsWithRentals = repository.getCarsWithRentals()
        }
    }
}