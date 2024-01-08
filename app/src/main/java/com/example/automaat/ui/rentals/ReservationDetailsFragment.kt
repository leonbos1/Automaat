package com.example.automaat.ui.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.automaat.R
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.toReadableString
import com.example.automaat.helpers.DateHelper

class ReservationDetailsFragment : Fragment() {
    private var reservationViewModel: ReservationDetailsViewModel? = null
    private var carWithRental: CarWithRental? = null
    private var startDateSpinner: Spinner? = null
    private var endDateSpinner: Spinner? = null
    private var priceTextView: TextView? = null

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

        startDateSpinner = view.findViewById(R.id.spinnerStartDate)
        endDateSpinner = view.findViewById(R.id.spinnerEndDate)
        priceTextView = view.findViewById(R.id.priceTextView)

        val dates = DateHelper.getDates()

        val startDateAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)

        var endDateAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)

        startDateSpinner?.adapter = startDateAdapter
        endDateSpinner?.adapter = endDateAdapter

        startDateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //update the end date spinner to statr from the selected start date
                endDateAdapter.clear()
                DateHelper.getPossibleEndDates(startDateSpinner?.selectedItem.toString()).value?.let {
                    endDateAdapter.addAll(
                        it
                    )
                }

                //update the price textview based on DateHelper.getDaysBetween(startDate, endDate)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        endDateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                //update the price textview based on DateHelper.getDaysBetween(startDate, endDate)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return view
    }
}