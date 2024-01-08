package com.example.automaat.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rentals")
data class RentalModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: String,
    val longitude: Float,
    val latitude: Float,
    var fromDate: String,
    var toDate: String,
    var state: RentalState,
    val inspections: Int?,
    val customerId: Int?,
    val carId: Int?
): Parcelable