package com.example.automaat.models.Car

import android.os.Parcel
import android.os.Parcelable

data class FilterModel(var brand: String?, var model: String?) : Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(brand)
        parcel.writeString(model)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FilterModel> {
        override fun createFromParcel(parcel: Parcel): FilterModel {
            return FilterModel(parcel)
        }

        override fun newArray(size: Int): Array<FilterModel?> {
            return arrayOfNulls(size)
        }
    }
}