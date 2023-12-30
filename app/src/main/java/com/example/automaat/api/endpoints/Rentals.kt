package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.example.automaat.api.synchers.RentalSyncManager
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
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Rentals {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    /**
     * Get all rentals from the backend server
     *
     * Returns a JsonArray of all rentals
     *
     * @return JsonArray?
     */
    suspend fun getAllRentals(): JsonArray? {
        return suspendCancellableCoroutine { continuation ->
            api.getAllRentals().enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body())
                    } else {
                        continuation.resumeWithException(
                            RuntimeException("Failed with ${response.code()}")
                        )
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
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