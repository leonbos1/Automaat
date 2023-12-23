package com.example.automaat.api.dataClasses

data class  Auth(
    val username: String,
    val password: String,
    val rememberMe: Boolean
)