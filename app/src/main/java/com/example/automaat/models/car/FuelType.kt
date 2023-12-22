package com.example.automaat.models.car

enum class FuelType {
    GASOLINE,
    DIESEL,
    HYBRID,
    ELECTRIC
}

fun FuelType.toReadableString(): String {
    return when (this) {
        FuelType.GASOLINE -> "Gasoline"
        FuelType.DIESEL -> "Diesel"
        FuelType.HYBRID -> "Hybrid"
        FuelType.ELECTRIC -> "Electric"
    }
}