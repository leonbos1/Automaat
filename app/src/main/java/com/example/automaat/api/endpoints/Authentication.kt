package com.example.automaat.api.endpoints

import android.content.Context
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

    fun authenticateApi(onAuthenticationComplete: () -> Unit) {
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

    fun authenticateUser(body: Auth, context: Context, onResult: (Boolean) -> Unit) {
        api.postAuthenticate(body).enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("id_token", response.body()?.get("id_token")?.asString)
                    editor.apply()
                    onResult(true)
                } else {
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i(TAG, "onFailure AuthenticateUser: ${t.message}")
                onResult(false)
            }
        })
    }

    fun isUserAuthenticated(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("id_token", null)
        println("User: $token")
        return token != null
    }
}