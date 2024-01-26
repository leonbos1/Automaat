package com.example.automaat.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "rentals")
data class RentalModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val code: String?,
    var longitude: Float,
    var latitude: Float,
    var fromDate: String,
    var toDate: String,
    var state: RentalState,
    var inspectionId: Int?,
    var customerId: Int?,
    val carId: Int?
): Parcelable