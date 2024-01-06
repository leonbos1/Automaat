package com.example.automaat.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.automaat.api.datamodels.Car
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "customers")
data class CustomerModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nr: String,
    val firstName: String,
    val lastName: String,
    val from: String,
    val location: String?
): Parcelable