package com.example.automaat.api.datamodels

data class  Auth(
    val username: String,
    val password: String,
    val rememberMe: Boolean
)