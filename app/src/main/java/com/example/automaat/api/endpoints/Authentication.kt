package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.example.automaat.api.datamodels.Auth
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Authentication {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"
    private val body = Auth("admin", "admin", true)

    fun authenticate(onAuthenticationComplete: () -> Unit) {
        api.postAuthenticate(body).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        ApiClient.setToken(it.get("id_token").asString)
                        Log.i(TAG, "onResponse Authenticate: $it")
                        onAuthenticationComplete()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure Authenticate: ${t.message}")
            }
        })
    }
}