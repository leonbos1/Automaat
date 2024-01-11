package com.example.automaat.api.datamodels

import java.util.Date

data class Rental(
    var id: Int?,
    val code: String,
    val longitude: Float,
    val latitude: Float,
    val fromDate: String,
    val toDate: String,
    val state: String,
    val inspections: Inspection?,
    val customer: Customer,
    val car: Car
)
