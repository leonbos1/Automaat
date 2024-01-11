package com.example.automaat.api.endpoints

import android.util.Log
import com.example.automaat.api.ApiClient
import com.example.automaat.api.InterfaceApi
import com.example.automaat.api.datamodels.Car
import com.example.automaat.api.datamodels.Customer
import com.example.automaat.api.datamodels.Rental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.entities.toUpperCaseString
import com.example.automaat.utils.SnackbarManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class Rentals {
    private val api = ApiClient.retrofit.create(InterfaceApi::class.java)
    private val TAG: String = "CHECK_RESPONSE"

    /**
     * Get all rentals from the backend server
     *
     * Returns a JsonArray of all rentals
     *
     * @return JsonArray?
     */
    suspend fun getAllRentals(): JsonArray? {
        return suspendCancellableCoroutine { continuation ->
            api.getAllRentals().enqueue(object : Callback<JsonArray> {
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

    suspend fun updateRental(rentalWithCarWithCustomer: RentalWithCarWithCustomer): Boolean {
        val rental = getRentalByRentalWithCarWithCustomer(rentalWithCarWithCustomer)
        return suspendCancellableCoroutine { continuation ->
            rental.id?.let {
                api.updateRental(it, rental).enqueue(object : Callback<RentalWithCarWithCustomer> {
                    override fun onResponse(
                        call: Call<RentalWithCarWithCustomer>,
                        response: Response<RentalWithCarWithCustomer>
                    ) {
                        if (response.isSuccessful) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    }

                    override fun onFailure(call: Call<RentalWithCarWithCustomer>, t: Throwable) {
                        continuation.resumeWithException(t)
                    }
                })
            }
        }
    }

    suspend fun addRental(rentalWithCarWithCustomer: RentalWithCarWithCustomer): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val rental = getRentalByRentalWithCarWithCustomer(rentalWithCarWithCustomer)
            rental.id = null
            api.addRental(rental).enqueue(object : Callback<RentalWithCarWithCustomer> {
                override fun onResponse(
                    call: Call<RentalWithCarWithCustomer>,
                    response: Response<RentalWithCarWithCustomer>
                ) {
                    if (response.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onFailure(call: Call<RentalWithCarWithCustomer>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    suspend fun getRentalById(id: Int) : JsonObject? {
        return suspendCancellableCoroutine { continuation ->
            api.getRentalById(id).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        continuation.resume(response.body())
                    } else {
                        continuation.resume(response.body())
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }


    /**
     * Get backend rental model using rental with car and customer
     *
     * This fix is needed because the backend expects nested models instead of ids :/
     */
    fun getRentalByRentalWithCarWithCustomer(rentalWithCarWithCustomer: RentalWithCarWithCustomer): Rental {
        return Rental(
            rentalWithCarWithCustomer.rental?.id ?: 0,
            rentalWithCarWithCustomer.rental?.code ?: "",
            rentalWithCarWithCustomer.rental?.longitude ?: 0.0f,
            rentalWithCarWithCustomer.rental?.latitude ?: 0.0f,
            rentalWithCarWithCustomer.rental?.fromDate ?: "",
            rentalWithCarWithCustomer.rental?.toDate ?: "",
            rentalWithCarWithCustomer.rental?.state?.toUpperCaseString() ?: "",
            null,
            Customer(
                rentalWithCarWithCustomer.customer?.id ?: 0,
                rentalWithCarWithCustomer.customer?.nr ?: 0,
                rentalWithCarWithCustomer.customer?.firstName ?: "",
                rentalWithCarWithCustomer.customer?.lastName ?: "",
                rentalWithCarWithCustomer.customer?.from ?: "",
                null
            ),
            Car(
                rentalWithCarWithCustomer.car?.id ?: 0,
                rentalWithCarWithCustomer.car?.brand ?: "",
                rentalWithCarWithCustomer.car?.model ?: "",
                rentalWithCarWithCustomer.car?.fuelType?.ordinal ?: 0,
                rentalWithCarWithCustomer.car?.options ?: "",
                rentalWithCarWithCustomer.car?.price ?: 0.0f,
                rentalWithCarWithCustomer.car?.licensePlate ?: "",
                rentalWithCarWithCustomer.car?.engineSize ?: 0,
                rentalWithCarWithCustomer.car?.numOfSeats ?: 0,
                rentalWithCarWithCustomer.car?.modelYear ?: 0,
                rentalWithCarWithCustomer.car?.since ?: "",
                rentalWithCarWithCustomer.car?.body?.ordinal ?: 0
            )
        )
    }
}