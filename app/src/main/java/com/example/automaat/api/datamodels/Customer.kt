package com.example.automaat.api.datamodels

import android.location.Location
import java.time.LocalDate

data class Customer(
    val id: Int,
    val nr: Int,
    val lastName: String,
    val firstName: String,
    val from: String,
    val location: Location?
)