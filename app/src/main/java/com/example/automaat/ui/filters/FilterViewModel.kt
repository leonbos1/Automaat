package com.example.automaat.ui.filters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.entities.CarModel
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.CustomerRepository
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<CarModel>>
    val carRepository: CarRepository
    val rentalRepository: RentalRepository
    val customerRepository: CustomerRepository
    val inspectionRepository: InspectionRepository
    private val _availableModels = MutableLiveData<List<String>>()

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        val customerDao = AutomaatDatabase.getDatabase(application).customerDao()
        val inspectionDao = AutomaatDatabase.getDatabase(application).inspectionDao()
        carRepository = CarRepository(carDao)
        rentalRepository = RentalRepository(rentalDao)
        customerRepository = CustomerRepository(customerDao)
        inspectionRepository = InspectionRepository(inspectionDao)
        readAllData = carRepository.readAllData
    }

    fun removeAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.deleteAllCars()
            rentalRepository.deleteAllRentals()
            customerRepository.deleteAllCustomers()
            inspectionRepository.deleteAllInspections()
        }
    }

    fun insertDummyCars() {
        carRepository.insertDummyCars()
    }

    val availableBrands: LiveData<List<String>> = carRepository.readAllData.map { cars ->
        val brands = cars.mapNotNull { it.brand }.distinct().sorted()
        listOf("All") + brands  // Prepend "All" to the list
    }

    val availableModels: LiveData<List<String>> = _availableModels.map { models ->
        listOf("All") + models.distinct().sorted()
    }

    fun loadModelsForBrand(brand: String) {
        viewModelScope.launch {
            val models = if (brand == "All") {
                // If "All" is selected, get all models
                carRepository.readAllData.value?.mapNotNull { it.model } ?: emptyList()
            } else {
                // Otherwise, filter models by the selected brand
                carRepository.getModelsByBrand(brand)
            }
            _availableModels.postValue(models)
        }
    }

}