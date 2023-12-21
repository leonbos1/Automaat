package com.example.automaat.ui.filters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.models.car.CarModel
import com.example.automaat.repositories.CarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FilterViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<CarModel>>
    private val repository: CarRepository
    private val _availableModels = MutableLiveData<List<String>>()

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        repository = CarRepository(carDao)
        readAllData = repository.readAllData
    }

    //TODO: Remove this function
    fun addCar(car: CarModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCar(car)
        }
    }

    fun insertDummyCars() {
        repository.insertDummyCars()
    }

    val availableBrands: LiveData<List<String>> = repository.readAllData.map { cars ->
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
                repository.readAllData.value?.mapNotNull { it.model } ?: emptyList()
            } else {
                // Otherwise, filter models by the selected brand
                repository.getModelsByBrand(brand)
            }
            _availableModels.postValue(models)
        }
    }

}