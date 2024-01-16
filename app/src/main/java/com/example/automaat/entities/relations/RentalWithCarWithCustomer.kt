package com.example.automaat.entities.relations

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.CustomerModel
import com.example.automaat.entities.RentalModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RentalWithCarWithCustomer(
    @Embedded val rental: RentalModel?,
    @Relation(
        parentColumn = "carId",
        entityColumn = "id"
    )
    val car: CarModel?,

    @Relation(
        parentColumn = "customerId",
        entityColumn = "id"
    )

    var customer: CustomerModel?
) : Parcelable
