package com.example.automaat.ui.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.InvalidationTracker
import com.example.automaat.AutomaatDatabase
import com.example.automaat.api.synchers.CarSyncManager
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.RentalRepository
import kotlinx.coroutines.launch

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    private val rentalRepository: RentalRepository
    private lateinit var rentalsByCustomer: LiveData<List<RentalWithCarWithCustomer>>
    private val hardcodedCustomer = 1

    //TODO add syncer stuff for reservations
    //    lateinit var carsSynchManager: CarSyncManager

    init {
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)

        //carsSynchManager = CarSyncManager(carRepository)

        viewModelScope.launch {
            rentalsByCustomer =
                rentalRepository.getRentalsWithCarAndCustomerByCustomer(hardcodedCustomer)
        }
    }

    fun getRentalsByCustomer(): LiveData<List<RentalWithCarWithCustomer>> {
        return rentalsByCustomer
    }

    fun getFutureRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val futureRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        getRentalsByCustomer().observe(lifecycleOwner, Observer { rentals ->
            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.RESERVED
            }
            futureRentals.value = filteredRentals
        })

        return futureRentals
    }

    fun getCurrentRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val currentRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        getRentalsByCustomer().observe(lifecycleOwner, Observer { rentals ->
            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.PICKUP || rental.rental?.state == RentalState.ACTIVE
            }
            currentRentals.value = filteredRentals
        })

        return currentRentals
    }

    fun getHistoricRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val historicRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        getRentalsByCustomer().observe(lifecycleOwner, Observer { rentals ->
            val filteredRentals = rentals.filter { rental ->
                rental.rental?.state == RentalState.RETURNED
            }
            historicRentals.value = filteredRentals
        })

        return historicRentals
    }
}