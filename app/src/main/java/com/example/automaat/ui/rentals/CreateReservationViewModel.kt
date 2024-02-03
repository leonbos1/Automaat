package com.example.automaat.ui.rentals

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.endpoints.Account
import com.example.automaat.api.syncers.RentalSyncManager
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import com.example.automaat.utils.NetworkMonitor
import kotlinx.coroutines.launch

class CreateReservationViewModel(application: Application) : AndroidViewModel(application) {
    val rentalWithCarWithCustomer: MutableLiveData<RentalWithCarWithCustomer> = MutableLiveData()
    private val rentalRepository: RentalRepository
    private val inspectionRepository: InspectionRepository
    private val customerId = Account().getUserIdFromSharedPreferences(getApplication<Application>().applicationContext)
    private var rentalSyncManager: RentalSyncManager? = null

    init {
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
        val inspectionDao = AutomaatDatabase.getDatabase(application).inspectionDao()
        inspectionRepository = InspectionRepository(inspectionDao)
        rentalSyncManager = RentalSyncManager(rentalRepository, application)
    }

    fun fetchRentalWithCarWithCustomer(carWithRental: CarWithRental) {
        viewModelScope.launch {
            val fetchedData =
                rentalRepository.getRentalsWithCarAndCustomerByCarId(carWithRental.car.id)

            if (fetchedData.isNotEmpty()) {
                rentalWithCarWithCustomer.value = fetchedData[0]
            } else {
                rentalWithCarWithCustomer.value = RentalWithCarWithCustomer(
                    null,
                    carWithRental.car,
                    null
                )
            }
        }
    }

    fun createReservation(
        rental: RentalWithCarWithCustomer,
        startDate: String,
        endDate: String,
        context: Context
    ) {
        if (rental.rental == null) {
            val newRental = getNewRental(rental.car!!.id)

            newRental.fromDate = startDate
            newRental.toDate = endDate
            newRental.state = RentalState.RESERVED
            newRental.customerId = customerId

            viewModelScope.launch {
                rentalRepository.insertRental(newRental)
                newRental.customerId = customerId
                rentalRepository.updateRental(newRental)
            }
        } else {
            rental.rental.apply {
                fromDate = startDate
                toDate = endDate
                state = RentalState.RESERVED
                customerId = this@CreateReservationViewModel.customerId
            }

            viewModelScope.launch {
                rentalRepository.updateRental(rental.rental)
            }
        }

        if (NetworkMonitor.isConnected) {
            rentalSyncManager?.syncEntities(context)
        }

    }

    fun generateId(): Int {
        return (2000..999999).random()
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
            customerId,
            carId
        )
    }
}