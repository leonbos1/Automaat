package com.example.automaat.ui.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.InspectionWithCarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val rentalRepository: RentalRepository
    private val inspectionRepository: InspectionRepository
    lateinit var rentalsByCustomer: LiveData<List<RentalWithCarWithCustomer>>
    val hardcodedCustomer = 1
    lateinit var inspection: LiveData<InspectionModel>

    init {
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
        inspectionRepository =
            InspectionRepository(AutomaatDatabase.getDatabase(application).inspectionDao())

        viewModelScope.launch {
            rentalsByCustomer =
                rentalRepository.getRentalsWithCarAndCustomerByCustomer(hardcodedCustomer)
        }
    }

    fun getFutureRentalsByCustomer(): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val futureRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        rentalsByCustomer.observeForever { rentals ->
            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.RESERVED
            }
            futureRentals.value = filteredRentals
        }

        return futureRentals
    }

    fun getCurrentRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val currentRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        rentalsByCustomer.observeForever { rentals ->

            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.PICKUP || rental.rental?.state == RentalState.ACTIVE
            }
            currentRentals.value = filteredRentals
        }

        return currentRentals
    }

    fun getHistoricRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val historicRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        rentalsByCustomer.observeForever { rentals ->
            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.RETURNED
            }
            historicRentals.value = filteredRentals
        }

        return historicRentals
    }

    fun getInspectionByRentalId(rentalId: Int) {
        viewModelScope.launch {
            inspection = inspectionRepository.getInspectionByRentalId(rentalId)
            println("FOUND INSPECTION WITH RENTALID $rentalId")
            println(inspection.value)
        }
    }

    private fun generateId(): Int {
        return (2000..999999).random()
    }
}