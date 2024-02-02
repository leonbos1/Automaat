package com.example.automaat.api.endpoints

import android.content.Context
import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Inspections {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    suspend fun getAllInspections(context: Context): JsonArray? {
        return suspendCancellableCoroutine { continuation ->
            val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("id_token", null)
            ApiClient.setToken(token)
            api.getAllInspections().enqueue(object : Callback<JsonArray> {
                override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body())
                    } else {
                        continuation.resume(JsonArray())
                    }
                }

                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    fun updateInspection(inspection: JsonObject, context: Context) {
        val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("id_token", null)
        ApiClient.setToken(token)
        api.updateInspection(inspection, inspection.get("id").asInt)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "Inspection updated")
                    } else {
                        Log.d(TAG, "Inspection update failed")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.d(TAG, "Inspection update failed")
                }
            })
    }
}