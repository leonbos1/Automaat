package com.example.automaat.api.endpoint

import android.util.Log
import com.example.automaat.api.InterfaceApi
import com.google.gson.JsonArray
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Routes (private val token: String) {
    private val BASE_URL = "https://seal-shining-hamster.ngrok-free.app/api/"
    private val TAG: String = "CHECK_RESPONSE"

    val authInterceptor = Interceptor { chain ->
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .method(original.method, original.body)

        val request = requestBuilder.build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(InterfaceApi::class.java)

    fun getAllRoutes() {
        api.getAllRoutes().enqueue(object: Callback<JsonArray> {
            override fun onResponse(call: Call<JsonArray>, response: Response<JsonArray>) {
                if (response.isSuccessful) {
                    if (response.body() is JsonArray) {
                        Log.i(TAG, "onResponse Route: ${response.body()}")
                    }
                }
            }

            override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                Log.i(TAG, "onFailure Routes: ${t.message}")
            }
        })
    }
}