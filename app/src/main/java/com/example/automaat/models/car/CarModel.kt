package com.example.automaat.models.car

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "cars")
data class CarModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val brand: String?,
    val model: String?,
    val fuelType: FuelType,
    val options: String?,
    val price: Float,
    val licensePlate: String?,
    val engineSize: Int,
    val numOfSeats: Int,
    val modelYear: Int,
    val since: String?,
    val body: Int,
    val rental: Int
): Parcelable