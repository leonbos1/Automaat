package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.example.automaat.entities.Body
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.FuelType
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.RentalState
import com.example.automaat.repositories.CarRepository
import com.example.automaat.repositories.RentalRepository
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Rentals(private val rentalRepository: RentalRepository) {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    fun getAllRentals() {
        api.getAllRentals().enqueue(object : Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (response.isSuccessful) {
                        Log.i(TAG, "onResponse Rentals: ${response.body()}")

                        val rental = response.body()?.get(0)?.asJsonObject
                        Log.i(TAG, "onResponse Rental: $rental")

                        rental?.let {
                            val carId = it.get("car").asJsonObject.get("id").asInt

                            val rentalModel = RentalModel(
                                it.get("id").asInt,
                                it.get("code").asString,
                                it.get("longitude").asFloat,
                                it.get("latitude").asFloat,
                                it.get("fromDate").asString,
                                it.get("toDate").asString,
                                RentalState.fromString(it.get("state").asString),
                                null,
                                null,
                                carId
                            )

                            rentalRepository.addRental(rentalModel)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i(TAG, "onFailure Rentals: ${t.message}")
            }
        })
    }

    fun getRentalById(id: Int) {
        api.getRentalById(id).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse RentalById: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure RentalById: ${t.message}")
            }
        })
    }
}