package com.example.automaat.api.endpoint

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InspectionPhotos {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    fun getAllInspectionPhotos() {
        api.getAllInspectionPhotos().enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse InspectionsPhotos: ${response.body()}")
                    // For loop to make each value a datatype.
                    // Store in DB
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i(TAG, "onFailure InspectionPhotos: ${t.message}")
            }
        })
    }

    fun getInspectionPhotoById(id: Int) {
        api.getInspectionPhotoById(id).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse InspectionPhotoById: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure InspectionPhotoById: ${t.message}")
            }
        })
    }
}