package com.example.automaat.entities

enum class FuelType {
    GASOLINE,
    DIESEL,
    HYBRID,
    ELECTRIC;

    companion object {
        fun fromString(fuel: String): FuelType {
            return when(fuel.lowercase()) {
                "gasoline" -> GASOLINE
                "diesel" -> DIESEL
                "electric" -> ELECTRIC
                "hybrid" -> HYBRID
                else -> throw IllegalArgumentException("Unknown fuel type: $fuel")
            }
        }
    }
}

fun FuelType.toReadableString(): String {
    return when (this) {
        FuelType.GASOLINE -> "Gasoline"
        FuelType.DIESEL -> "Diesel"
        FuelType.HYBRID -> "Hybrid"
        FuelType.ELECTRIC -> "Electric"
    }
}