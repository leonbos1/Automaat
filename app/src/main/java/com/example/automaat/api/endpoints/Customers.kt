package com.example.automaat.api.endpoints

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

class Customers {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    suspend fun getAllCustomers(): JsonArray? {
        return suspendCancellableCoroutine { continuation ->
            api.getAllCustomers().enqueue(object : Callback<JsonArray> {
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

    fun getCustomerById(id: Int) {
        api.getCustomerById(id).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    Log.i(TAG, "onResponse CustomerById: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure CustomerById: ${t.message}")
            }
        })
    }
}