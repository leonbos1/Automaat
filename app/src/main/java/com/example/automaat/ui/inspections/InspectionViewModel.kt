package com.example.automaat.ui.inspections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.entities.InspectionModel
import com.example.automaat.repositories.InspectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InspectionViewModel(application: Application) : AndroidViewModel(application) {
    private val inspectionRepository: InspectionRepository
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