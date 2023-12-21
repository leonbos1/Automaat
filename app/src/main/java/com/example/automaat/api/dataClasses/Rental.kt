package com.example.automaat.api.dataClasses

import java.util.Date

data class Rental(
    val id: Int,
    val code: String,
    val longitude: Float,
    val latitude: Float,
    val fromDate: Date,
    val toDate: Date,
    val state: String,
    val inspections: Inspection,
    val customer: Customer,
    val car: Car
)
