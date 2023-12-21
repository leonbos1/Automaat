package com.example.automaat.ui.filters

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.automaat.models.car.CarModel
import com.example.automaat.repositories.CarRepository

class FilterViewModel(application: Application) : AndroidViewModel() {
    val readAllData: LiveData<List<CarModel>>
    private val repository: CarRepository

    init {
        
    }
}