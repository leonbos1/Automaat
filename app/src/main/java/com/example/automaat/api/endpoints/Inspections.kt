package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Inspections {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    fun getAllInspections() {
        api.getAllInspections().enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse Inspections: ${response.body()}")
                    // For loop to make each value a datatype.
                    // Store in DB
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i(TAG, "onFailure Inspections: ${t.message}")
            }
        })
    }

    fun getInspectionById(id: Int) {
        api.getInspectionById(id).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse InspectionById: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure InspectionById: ${t.message}")
            }
        })
    }
}