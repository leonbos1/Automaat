package com.example.automaat.ui.rentals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.automaat.api.datamodels.Auth
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.api.synchers.RentalSyncManager
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val _rentalWithCarWithCustomer = MutableLiveData<RentalWithCarWithCustomer>()
    var testRentalWithCarWithCustomer: RentalWithCarWithCustomer? = null
    val rentalWithCarWithCustomer: LiveData<RentalWithCarWithCustomer> = _rentalWithCarWithCustomer
    val rentalRepository: RentalRepository
    private val hardcodedCustomer = 1
    private val _addRentalResult = MutableLiveData<Boolean>()
    val addRentalResult: LiveData<Boolean> = _addRentalResult
    var latestRentalWithCarWithCustomer: RentalWithCarWithCustomer? = null
    var rentalSyncManager: RentalSyncManager? = null

    init {
        val rentalDao = com.example.automaat.AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
        rentalSyncManager = RentalSyncManager(rentalRepository)
    }

    fun fetchRentalWithCarWithCustomer(carWithRental: CarWithRental) {
        viewModelScope.launch {
            val rentalId = carWithRental.rental?.id ?: 0
            val fetchedData = rentalRepository.getRentalsWithCarAndCustomerByRental(rentalId)
            fetchedData.observeForever { rentalList ->
                if (rentalList.isNotEmpty()) {
                    _rentalWithCarWithCustomer.value = rentalList[0]
                } else {
                    _rentalWithCarWithCustomer.value = RentalWithCarWithCustomer(
                        null,
                        carWithRental.car,
                        null
                    )
                }
            }
        }
    }

    fun createReservation(
        rental: RentalWithCarWithCustomer,
        startDate: String,
        endDate: String,
        lifecycleOwner: LifecycleOwner
    ) {
        //car doesnt have a rental yet
        if (rental.rental == null) {
            val newRental = getNewRental(rental.car!!.id)

            newRental.fromDate = startDate
            newRental.toDate = endDate
            newRental.state = RentalState.RESERVED

            viewModelScope.launch {
                rentalRepository.insertRental(newRental)
            }
        //car already has a rental
        } else {
            rental.rental.apply {
                fromDate = startDate
                toDate = endDate
                state = RentalState.RESERVED
            }

            viewModelScope.launch {
                rentalRepository.updateRental(rental.rental)
            }
        }

    }

    fun generateId(): Int {
        return (100000..999999).random()
    }

    fun getNewRental(carId: Int): RentalModel {
        return RentalModel(
            generateId(),
            "code",
            0.0f,
            0.0f,
            "",
            "",
            RentalState.RESERVED,
            0,
            hardcodedCustomer,
            carId
        )
    }
}