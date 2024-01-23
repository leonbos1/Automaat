package com.example.automaat.ui.inspections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.InspectionWithCarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InspectionViewModel(application: Application) : AndroidViewModel(application) {
    private val inspectionRepository: InspectionRepository
    private val hardcodedCustomer = 1
    lateinit var inspection: InspectionModel


    init {
        val inspectionDao = AutomaatDatabase.getDatabase(application).inspectionDao()
        inspectionRepository = InspectionRepository(inspectionDao)

        viewModelScope.launch {
        }
    }

    fun updateInspection(inspectionModel: InspectionModel) {
        viewModelScope.launch(Dispatchers.IO) {
            inspectionRepository.updateInspection(inspectionModel)
        }
    }
}