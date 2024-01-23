package com.example.automaat.ui.reservations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.automaat.AutomaatDatabase
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
    private lateinit var rentalsByCustomer: List<RentalWithCarWithCustomer>
    private val hardcodedCustomer = 1
    val inspectionWithCarWithRental = MutableLiveData<InspectionWithCarWithRental>()
    lateinit var inspection: InspectionWithCarWithRental

    //TODO add syncer stuff for reservations
    //    lateinit var carsSynchManager: CarSyncManager

    init {
        val rentalDao = AutomaatDatabase.getDatabase(application).rentalDao()
        rentalRepository = RentalRepository(rentalDao)
        inspectionRepository =
            InspectionRepository(AutomaatDatabase.getDatabase(application).inspectionDao())

        viewModelScope.launch(Dispatchers.IO) {
            val rentals = async {
                rentalsByCustomer =
                    rentalRepository.getRentalsWithCarAndCustomerByCustomer(hardcodedCustomer)
            }

            rentals.await()

            fetchInspectionWithCustomerWithRental(rentalsByCustomer.get(0))
        }
    }

    fun getRentalsByCustomer(): List<RentalWithCarWithCustomer> {
        if (::rentalsByCustomer.isInitialized) {
            return rentalsByCustomer
        } else {
            // Handle the case where rentalsByCustomer is not initialized
            // Maybe return an empty list or throw a custom exception
            return emptyList()
        }
    }

    fun getFutureRentalsByCustomer(): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val futureRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        val rentals = getRentalsByCustomer()
        val filteredRentals = rentals.filter { rental ->
            rental.rental?.state == RentalState.RESERVED
        }
        futureRentals.value = filteredRentals

        return futureRentals
    }

    fun getCurrentRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val currentRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        val rentals = getRentalsByCustomer()
        val filteredRentals = rentals.filter { rental ->
            rental.rental?.state == RentalState.PICKUP || rental.rental?.state == RentalState.ACTIVE
        }
        currentRentals.value = filteredRentals

        return currentRentals
    }

    fun getHistoricRentalsByCustomer(lifecycleOwner: LifecycleOwner): MutableLiveData<List<RentalWithCarWithCustomer>> {
        val historicRentals = MutableLiveData<List<RentalWithCarWithCustomer>>()

        val rentals = getRentalsByCustomer()
        val filteredRentals = rentals.filter { rental ->
            rental.rental?.state == RentalState.RETURNED
        }
        historicRentals.value = filteredRentals

        return historicRentals
    }

    suspend fun fetchInspectionWithCustomerWithRental(rental: RentalWithCarWithCustomer) : InspectionWithCarWithRental {
        var inspectionData =
            inspectionRepository.getInspectionWithCarWithRentalByCarId(rental.car!!.id)

        if (inspectionData == null) {
            inspectionRepository.createNewInspection(
                generateId(),
                rental.rental!!.id,
                rental.car.id
            )

            inspectionData =
                inspectionRepository.getInspectionWithCarWithRentalByCarId(rental.car.id)
        }

        return inspectionData
    }

    private fun generateId(): Int {
        return (2000..999999).random()
    }
}