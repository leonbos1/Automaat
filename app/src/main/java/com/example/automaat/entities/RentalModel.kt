package com.example.automaat.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.automaat.api.datamodels.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rentals")
data class RentalModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: String,
    val longitude: Float,
    val latitude: Float,
    val fromDate: String,
    val toDate: String,
    val state: RentalState,
    val inspections: Int?,
    val customer: Int?,
    val carId: Int?
): Parcelable