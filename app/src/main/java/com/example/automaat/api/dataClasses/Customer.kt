package com.example.automaat.api.dataClasses

import android.location.Location
import java.time.LocalDate

data class Customer(
    val id: Int,
    val nr: Int,
    val lastName: String,
    val firstName: String,
    val from: LocalDate,
    val systemUser: String, // Not sure if string or a User class
    val rentals: Boolean, // Not sure if bool
    val location: Location

)
