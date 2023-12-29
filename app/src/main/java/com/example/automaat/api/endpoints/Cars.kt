package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.example.automaat.api.synchers.CarSyncManager
import com.example.automaat.repositories.CarRepository
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Cars(private val carRepository: CarRepository) {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"
    private val carsSynchManager = CarSyncManager(carRepository)

    fun getAllCars() {
        api.getAllCars().enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (response.isSuccessful) {
                        carsSynchManager.syncEntities(response.body()!!)
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i(TAG, "onFailure Cars: ${t.message}")
            }
        })
    }

    fun getCarById(id: Int) {
        api.getCarById(id).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse CarById: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure CarById: ${t.message}")
            }

        })
    }
}