package com.example.automaat.ui.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer
import com.example.automaat.entities.toReadableString
import com.example.automaat.helpers.DateHelper

class CreateReservationFragment : Fragment() {
    private var reservationViewModel: CreateReservationViewModel? = null
    private var brandTextView: TextView? = null
    private var modelTextView: TextView? = null
    private var rentalTextView: TextView? = null
    private var startDateSpinner: Spinner? = null
    private var endDateSpinner: Spinner? = null
    private var priceTextView: TextView? = null
    private var reserveButton: Button? = null
    private var carWithRental: CarWithRental? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_reservation, container, false)

        reservationViewModel = ViewModelProvider(this).get(CreateReservationViewModel::class.java)

        carWithRental = arguments?.getParcelable("carWithRental")

        brandTextView = view.findViewById(R.id.carBrandTitleTextView)
        modelTextView = view.findViewById(R.id.carModelTitleTextView)
        rentalTextView = view.findViewById(R.id.rentalStatus)
        startDateSpinner = view.findViewById(R.id.spinnerStartDate)
        endDateSpinner = view.findViewById(R.id.spinnerEndDate)
        priceTextView = view.findViewById(R.id.priceTextView)
        reserveButton = view.findViewById(R.id.buttonReserve)

        brandTextView?.text = reservationViewModel!!.rental.car?.brand
        modelTextView?.text = reservationViewModel!!.rental.car?.model
        rentalTextView?.text = reservationViewModel!!.rental.rental?.state?.toReadableString()

        val dates = DateHelper.getDates()

        val startDateAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates)
        startDateSpinner?.adapter = startDateAdapter

        startDateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedStartDate = startDateSpinner?.selectedItem as String

                val possibleEndDates = DateHelper.getPossibleEndDates(selectedStartDate)

                val endDateAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    possibleEndDates
                )
                endDateSpinner?.adapter = endDateAdapter

                updatePrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        endDateSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updatePrice()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        reserveButton?.setOnClickListener {
            val startDate = startDateSpinner?.selectedItem as String
            val endDate = endDateSpinner?.selectedItem as String

            reservationViewModel!!.createReservation(
                reservationViewModel!!.rental,
                startDate,
                endDate
            )

            rentalTextView?.text = RentalState.RESERVED.toReadableString()

            var navController = findNavController()

            navController.popBackStack(R.id.create_reservation, false)
            navController.navigate(R.id.navigation_home)
        }

        return view
    }

    private fun updatePrice() {
        val startDate = startDateSpinner?.selectedItem as String
        val endDate = endDateSpinner?.selectedItem as String

        val rentalDuration = DateHelper.getDaysBetween(startDate, endDate)

        val price = reservationViewModel!!.rental.car?.price?.times(rentalDuration)

        priceTextView?.text = price.toString()
    }
}