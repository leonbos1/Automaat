package com.example.automaat.ui.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.automaat.R
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.toReadableString

class ReservationDetailsFragment : Fragment() {
    private var reservationViewModel: ReservationDetailsViewModel? = null
    private var carWithRental: CarWithRental? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reservation_details, container, false)

        reservationViewModel = ViewModelProvider(this).get(ReservationDetailsViewModel::class.java)

        carWithRental = arguments?.getParcelable("carWithRental")

        view.findViewById<TextView>(R.id.carBrandTitleTextView).text = carWithRental?.car?.brand
        view.findViewById<TextView>(R.id.carModelTitleTextView).text = carWithRental?.car?.model

        // TODO: wat doen als een car nog geen rental heeft? Deze fix laat alleen active op het scherm zien.
        if (carWithRental?.rental != null) {
            view.findViewById<TextView>(R.id.rentalStatus).text =
                carWithRental?.rental?.state?.toReadableString()
        } else {
            view.findViewById<TextView>(R.id.rentalStatus).text = "Active"
        }

        return view
    }
}