package com.example.automaat.models.rental

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class RentalModel(
    val id: Int,
    val code: String?,
    val longitude: Float,
    val latitude: Float,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val rentalState: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readSerializable() as LocalDate,
        parcel.readSerializable() as LocalDate,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RentalModel> {
        override fun createFromParcel(parcel: Parcel): RentalModel {
            return RentalModel(parcel)
        }

        override fun newArray(size: Int): Array<RentalModel?> {
            return arrayOfNulls(size)
        }
    }
}
