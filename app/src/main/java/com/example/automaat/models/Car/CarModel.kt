package com.example.automaat.models.Car

import android.os.Parcel
import android.os.Parcelable

data class CarModel(
    val id: Int,
    val brand: String?,
    val model: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(brand)
        parcel.writeString(model)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CarModel> {
        override fun createFromParcel(parcel: Parcel): CarModel {
            return CarModel(parcel)
        }

        override fun newArray(size: Int): Array<CarModel?> {
            return arrayOfNulls(size)
        }

        fun getAutoId(): Int {
            return (100000..999999).random()
        }
    }
}
