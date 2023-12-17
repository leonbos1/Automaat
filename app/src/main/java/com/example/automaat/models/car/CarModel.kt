package com.example.automaat.models.car

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class CarModel(
    val id: Int,
    val brand: String?,
    val model: String?,
    val fuelType: Int = 0,
    val options: String?,
    val price: Float = 0.0f,
    val licensePlate: String?,
    val engineSize: Int = 0,
    val numOfSeats: Int = 0,
    val modelYear: Int = 0,
    val since: LocalDate = LocalDate.now(),
    val body: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readSerializable() as LocalDate,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(brand)
        parcel.writeString(model)
        parcel.writeInt(fuelType)
        parcel.writeString(options)
        parcel.writeFloat(price)
        parcel.writeString(licensePlate)
        parcel.writeInt(engineSize)
        parcel.writeInt(numOfSeats)
        parcel.writeInt(modelYear)
        parcel.writeSerializable(since)
        parcel.writeInt(body)
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
