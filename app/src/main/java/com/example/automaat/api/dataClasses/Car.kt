package com.example.automaat.api.dataClasses

import java.time.Year
import java.util.Date

data class Car (
    val id: Int,
    val brand: String,
    val model: String,
    val fuel: String,
    val options: String,
    val licensePlate: String,
    val engineSize: Int,
    val modelYear: Year,
    val since: Date,
    val price: Float,
    val nrOfSeats: Int,
    val body: String,
    val inspections: Boolean,   // Not sure if it's a bool
    val repairs: Boolean,   // Not sure if it's a bool
    val rentals: Boolean   // Not sure if it's a bool
    )