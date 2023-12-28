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
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<CarModel>>
    public val carRepository: CarRepository
    public val rentalRepository: RentalRepository
    private val _availableModels = MutableLiveData<List<String>>()

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        carRepository = CarRepository(carDao)
        rentalRepository = RentalRepository(rentalDao)
        readAllData = carRepository.readAllData
    }

    fun removeAllCars() {
        viewModelScope.launch(Dispatchers.IO) {
            carRepository.deleteAllCars()
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