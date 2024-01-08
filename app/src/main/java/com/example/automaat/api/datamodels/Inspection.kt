package com.example.automaat.api.datamodels

import java.util.Date

data class Inspection(
    val id: String,
    val code: String,
    val odometer: Int,
    val result: String,
    val photo: String,
    val photoContentType: String,
    val completed: String
)
