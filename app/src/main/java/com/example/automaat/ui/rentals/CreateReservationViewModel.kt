package com.example.automaat.ui.rentals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
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
    private val rentalRepository: RentalRepository
    private val hardcodedCustomer = 1
    private val _addRentalResult = MutableLiveData<Boolean>()
    val addRentalResult: LiveData<Boolean> = _addRentalResult
    var latestRentalWithCarWithCustomer: RentalWithCarWithCustomer? = null

    init {
        val rentalDao = com.example.automaat.AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
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
        if (rental.rental == null) {
            // Insert new rental
            val newRental = RentalModel(
                generateId(),
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
            viewModelScope.launch {
                rentalRepository.insertRental(newRental)
            }

            rentalRepository.getRentalsWithCarAndCustomerByRental(newRental.id)
                .observe(lifecycleOwner, Observer { rentalList ->
                    if (rentalList.isNotEmpty()) {
                        val newRentalWithCarWithCustomer = rentalList[0]
                        Authentication().authenticate {
                            CoroutineScope(viewModelScope.coroutineContext).launch {
                                val result = Rentals().addRental(newRentalWithCarWithCustomer)
                                _addRentalResult.postValue(result)
                            }
                        }
                    }
                })
        } else {
            // Update existing rental
            rental.rental.apply {
                fromDate = startDate
                toDate = endDate
                state = RentalState.RESERVED
            }

            viewModelScope.launch {
                rentalRepository.updateRental(rental.rental)
            }

            Authentication().authenticate {
                viewModelScope.launch {
                    val result = Rentals().addRental(rental)
                    _addRentalResult.postValue(result)
                }
            }
        }
    }

    fun generateId(): Int {
        return (100000..999999).random()
    }
}