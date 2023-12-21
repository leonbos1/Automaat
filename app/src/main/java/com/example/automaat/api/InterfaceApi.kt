package com.example.automaat.api;

import com.example.automaat.api.dataClasses.Auth
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface InterfaceApi {
    @POST("authenticate")
    fun postAuthenticate(@Body user: Auth): Call<JsonObject>

    @GET("routes")
    fun getAllRoutes(): Call<JsonArray>

    @GET("routes/{id}")
    fun getRouteById(@Path("id") id: Int): Call<JsonObject>

    @GET("cars")
    fun getAllCars(): Call<JsonArray>

    @GET("cars/{id}")
    fun getCarById(@Path("id") id: Int): Call<JsonObject>

    @GET("customers")
    fun getAllCustomers(): Call<JsonArray>

    @GET("customers/{id}")
    fun getCustomerById(@Path("id") id: Int): Call<JsonObject>

    @GET("rentals")
    fun getAllRentals(): Call<JsonArray>

    @GET("rentals/{id}")
    fun getRentalById(@Path("id") id: Int): Call<JsonObject>

    @GET("inspections")
    fun getAllInspections(): Call<JsonArray>

    @GET("inspections/{id}")
    fun getInspectionById(@Path("id") id: Int): Call<JsonObject>

    @GET("inspection-photos")
    fun getAllInspectionPhotos(): Call<JsonArray>

    @GET("inspection-photos/{id}")
    fun getInspectionPhotoById(@Path("id") id: Int): Call<JsonObject>
}