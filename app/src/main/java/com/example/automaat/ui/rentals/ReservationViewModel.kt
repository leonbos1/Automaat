package com.example.automaat.ui.rentals

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.automaat.entities.relations.CarWithRental
import kotlinx.parcelize.Parcelize

class ReservationViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var carWithRental: CarWithRental
}