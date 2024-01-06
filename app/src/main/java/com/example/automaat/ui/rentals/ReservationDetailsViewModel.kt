package com.example.automaat.ui.rentals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.automaat.entities.relations.CarWithRental

class ReservationDetailsViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var carWithRental: CarWithRental
}