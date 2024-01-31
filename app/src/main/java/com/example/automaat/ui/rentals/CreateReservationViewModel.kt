package com.example.automaat.ui.rentals

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.automaat.api.synchers.RentalSyncManager
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.datamodels.Auth
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.api.synchers.RentalSyncManager
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.InspectionRepository
import com.example.automaat.repositories.RentalRepository
import com.example.automaat.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreateReservationViewModel(application: Application) : AndroidViewModel(application) {
    val rentalWithCarWithCustomer: MutableLiveData<RentalWithCarWithCustomer> = MutableLiveData()
    private val rentalRepository: RentalRepository
    private val inspectionRepository: InspectionRepository
    private val hardcodedCustomer = 1
    private var rentalSyncManager: RentalSyncManager? = null

    init {
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)tomaatDatabase.getDatabase(application).inspectionDao()
        inspectionRepository = InspectionRepository(inspectionDao)
        rentalSyncManager = RentalSyncManager(rentalRepository)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun createReservation(
        rental: RentalWithCarWithCustomer,
        startDate: String,
        endDate: String,
        lifecycleOwner: LifecycleOwner
    ) {
        if (rental.rental == null) {
            val newRental = getNewRental(rental.car!!.id)

            newRental.fromDate = startDate
            newRental.toDate = endDate
            newRental.state = RentalState.RESERVED
            newRental.customerId = hardcodedCustomer

            viewModelScope.launch {
                rentalRepository.insertRental(newRental)
                newRental.customerId = hardcodedCustomer
                rentalRepository.updateRental(newRental)
            }
        } else {
            rental.rental.apply {
                fromDate = startDate
                toDate = endDate
                state = RentalState.RESERVED
                customerId = hardcodedCustomer
            }

            viewModelScope.launch {
                rentalRepository.updateRental(rental.rental)
            }
        }

        if (NetworkMonitor.isConnected) {
            rentalSyncManager?.syncEntities()
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
            hardcodedCustomer,
            carId
        )
    }
}