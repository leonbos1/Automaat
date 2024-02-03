package com.example.automaat.api.endpoints

import android.content.Context
import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.google.gson.JsonObject
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Account {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    suspend fun getAccount(context: Context): JsonObject? {
        return suspendCancellableCoroutine {
            val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("id_token", null)
            ApiClient.setToken(token)
            api.getAccount().enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val sharedPreferences = context.getSharedPreferences("AccountResponse", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("account_data", response.body().toString())
                            apply()
                        }
                    } else {
                        Log.i(TAG, "onFailure Account: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i(TAG, "onFailure Account: ${t.message}")
                }
            })
        }
    }

    fun getUserIdFromSharedPreferences(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences("AccountResponse", Context.MODE_PRIVATE)
        val accountDataString = sharedPreferences.getString("account_data", null)

        if (accountDataString != null) {
            try {
                val jsonObj = JSONObject(accountDataString)
                return jsonObj.getInt("id")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return null
    }
}