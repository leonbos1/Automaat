package com.example.automaat.entities

enum class Body {
    STATIONWAGON,
    SEDAN,
    HATCHBACK,
    MINIVAN,
    MPV,
    SUV,
    COUPE,
    TRUCK,
    CONVERTIBLE;

    companion object {
        fun fromString(body: String): Body {
            return when(body.lowercase()) {
                "stationwagon" -> STATIONWAGON
                "sedan" -> SEDAN
                "hatchback" -> HATCHBACK
                "minivan" -> MINIVAN
                "mpv" -> MPV
                "suv" -> SUV
                "coupe" -> COUPE
                "truck" -> TRUCK
                "convertible" -> CONVERTIBLE
                else -> throw IllegalArgumentException("Unknown body type: $body")
            }
        }
    }
}