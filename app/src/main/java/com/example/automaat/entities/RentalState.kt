package com.example.automaat.entities

enum class RentalState {
    ACTIVE,
    RETURNED,
    PICKUP,
    RESERVED;

    companion object {
        fun fromString(rental: String): RentalState {
            return when(rental.lowercase()) {
                "active" -> ACTIVE
                "returned" -> RETURNED
                "pickup" -> PICKUP
                "reserved" -> RESERVED
                else -> throw IllegalArgumentException("Unknown rental state: $rental")
            }
        }
    }
}

fun RentalState.toReadableString(): String {
    return when (this) {
        RentalState.ACTIVE -> "Active"
        RentalState.RETURNED -> "Returned"
        RentalState.PICKUP -> "Pickup"
        RentalState.RESERVED -> "Reserved"
    }
}