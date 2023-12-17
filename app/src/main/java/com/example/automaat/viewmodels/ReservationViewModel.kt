package com.example.automaat.viewmodels

import android.os.Parcel
import android.os.Parcelable
import com.example.automaat.models.car.CarModel
import com.example.automaat.models.rental.RentalModel

data class ReservationViewModel(
    val car: CarModel?,
    val rental: RentalModel?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CarModel::class.java.classLoader),
        parcel.readParcelable(RentalModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(car, flags)
        parcel.writeParcelable(rental, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReservationViewModel> {
        override fun createFromParcel(parcel: Parcel): ReservationViewModel {
            return ReservationViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ReservationViewModel?> {
            return arrayOfNulls(size)
        }
    }

}