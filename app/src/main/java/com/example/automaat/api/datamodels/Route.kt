package com.example.automaat.api.datamodels

import java.time.LocalDate

data class  Route(
    val id: Int,
    val code: String,
    val description: String,
    val localDate: LocalDate
)
