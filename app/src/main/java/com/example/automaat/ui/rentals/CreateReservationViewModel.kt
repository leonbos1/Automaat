package com.example.automaat.ui.rentals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CreateReservationViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var rental: RentalWithCarWithCustomer
    private val _rentalLiveData = MutableLiveData<RentalWithCarWithCustomer?>()
    val rentalLiveData: LiveData<RentalWithCarWithCustomer?>
        get() = _rentalLiveData
    private val rentalRepository: RentalRepository
    private val hardcodedCustomer = 1
    private lateinit var rentalsByCustomer: LiveData<List<RentalWithCarWithCustomer>>

    init {
        val rentalDao = com.example.automaat.AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
    }

    fun createReservation(rental: RentalWithCarWithCustomer, startDate: String, endDate: String) {
        if (rental.rental == null) {
            viewModelScope.launch {
                rentalRepository.insertRental(
                    RentalModel(
                        0,
                        "code",
                        0.0f,
                        0.0f,
                        startDate,
                        endDate,
                        com.example.automaat.entities.RentalState.RESERVED,
                        0,
                        hardcodedCustomer,
                        rental.car?.id
                    )
                )
            }
            return
        }

        rental.rental.fromDate = startDate
        rental.rental.toDate = endDate
        rental.rental.state = com.example.automaat.entities.RentalState.RESERVED

        viewModelScope.launch {
            rentalRepository.updateRental(rental.rental)
            Authentication().authenticate {
                CoroutineScope(viewModelScope.coroutineContext).launch {
                    Rentals().updateRental(rental)
                }
            }
        }
    }
}