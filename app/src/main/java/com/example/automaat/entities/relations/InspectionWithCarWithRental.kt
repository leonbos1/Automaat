package com.example.automaat.entities.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.InspectionModel
import com.example.automaat.entities.RentalModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class InspectionWithCarWithRental(
    @Embedded val inspection: InspectionModel?,

    @Relation(
        parentColumn = "carId",
        entityColumn = "id"
    )

    var car: CarModel?,

    @Relation(
        parentColumn = "rentalId",
        entityColumn = "id"
    )
    val rental: RentalModel?
) : Parcelable
