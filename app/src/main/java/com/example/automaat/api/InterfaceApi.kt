package com.example.automaat.api;

import com.example.automaat.api.dataClasses.Auth
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface InterfaceApi {
    @POST("authenticate")
    fun postAuthenticate(@Body user: Auth): Call<JsonObject>

    @GET("routes")
    fun getAllRoutes(): Call<JsonArray>
}