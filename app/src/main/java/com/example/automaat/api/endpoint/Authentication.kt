package com.example.automaat.api.endpoint

import android.util.Log
import com.example.automaat.api.InterfaceApi
import com.example.automaat.api.dataClasses.Auth
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Authentication {
    private val BASE_URL = "https://seal-shining-hamster.ngrok-free.app/api/"
    private val TAG: String = "CHECK_RESPONSE"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(InterfaceApi::class.java)

    val body = Auth("admin", "admin", true)

    private var token = ""


    private fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String {
        return this.token
    }

    fun authenticate(onAuthenticationComplete: () -> Unit) {

        api.postAuthenticate(body).enqueue(object: Callback<JsonObject> {

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    if (response.body() is JsonObject) {
                        setToken(response.body()!!.get("id_token").asString)
                        Log.i(TAG, "onResponse Authenticate: ${response.body()}")
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