package com.example.automaat.api.datamodels

import com.example.automaat.entities.Body
import com.example.automaat.entities.FuelType
import com.example.automaat.entities.RentalModel

data class Car (
    val id: Int,
    val brand: String?,
    val model: String?,
    val fuelType: FuelType,
    val options: String?,
    val price: Float,
    val licensePlate: String?,
    val engineSize: Int,
    val numOfSeats: Int,
    val modelYear: Int,
    val since: String?,
    val body: Body?
    )