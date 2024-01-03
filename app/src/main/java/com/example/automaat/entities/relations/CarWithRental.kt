package com.example.automaat.entities.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.RentalModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarWithRental(
    @Embedded val car: CarModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "carId"
    )
    val rental: RentalModel?
) : Parcelable
