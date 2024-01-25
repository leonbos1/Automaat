package com.example.automaat.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.automaat.api.datamodels.Employee
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "inspections")
data class InspectionModel (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val code: String?,
    val odometer: Int?,
    var result: String?,
    var photo: String?,
    val photoContentType: String?,
    val completed: String?,
    val photos: String?, //won't use as it will increase complexity
    val carId: Int?,
    val employeeId: Int?,
    val rentalId: Int?
): Parcelable