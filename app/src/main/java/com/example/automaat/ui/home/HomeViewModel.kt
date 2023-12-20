package com.example.automaat.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.automaat.AutomaatDatabase
import com.example.automaat.models.car.CarModel
import com.example.automaat.repositories.CarRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<CarModel>>
    private val repository: CarRepository

    init {
        val carDao = AutomaatDatabase.getDatabase(application).carDao()
        repository = CarRepository(carDao)
        readAllData = repository.readAllData
    }

    fun insertDummyCars() {
        repository.insertDummyCars()
    }

    fun getAvailableModelsByBrand(brand: String): ArrayList<String> {
        return repository.getAvailableModelsByBrand(brand)
    }

    fun getAvailableBrands(): ArrayList<String> {
        return repository.getAvailableBrands()
    }

    fun getAvailableModels(): ArrayList<String> {
        return repository.getAvailableModels()
    }
}