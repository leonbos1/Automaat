package com.example.automaat.ui.rentals

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental
import com.example.automaat.entities.toReadableString
import com.example.automaat.helpers.DateHelper
import com.example.automaat.utils.SnackbarManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class CreateReservationFragment : Fragment() {
    private var reservationViewModel: CreateReservationViewModel? = null
    private var brandTextView: TextView? = null
    private var modelTextView: TextView? = null
    private var rentalTextView: TextView? = null
    private var priceTextView: TextView? = null
    private var reserveButton: Button? = null
    private var carWithRental: CarWithRental? = null
    private var startDateTextView: TextView? = null
    private var endDateTextView: TextView? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_reservation, container, false)

        reservationViewModel = ViewModelProvider(this).get(CreateReservationViewModel::class.java)

        carWithRental = arguments?.getParcelable("carWithRental")

        reservationViewModel!!.fetchRentalWithCarWithCustomer(carWithRental!!)

        priceTextView = view.findViewById(R.id.priceTextView)
        reserveButton = view.findViewById(R.id.buttonReserve)
        startDateTextView = view.findViewById(R.id.textViewStartDate)
        endDateTextView = view.findViewById(R.id.textViewEndDate)

        reservationViewModel!!.rentalWithCarWithCustomer.observe(
            viewLifecycleOwner
        ) { rentalWithCarWithCustomer ->
            brandTextView?.text = rentalWithCarWithCustomer.car?.brand
            modelTextView?.text = rentalWithCarWithCustomer.car?.model
            rentalTextView?.text = rentalWithCarWithCustomer.rental?.state?.toReadableString()
        }

        startDateTextView?.setOnClickListener {
            showStartDatePicker()
        }

        endDateTextView?.setOnClickListener {
            showEndDatePicker(
                LocalDate.parse(
                    startDateTextView?.text.toString(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
            )
        }

        reservationViewModel!!.rentalWithCarWithCustomer.observe(viewLifecycleOwner) { rentalWithCarWithCustomer ->
            reserveButton?.setOnClickListener {
                var startDate = startDateTextView?.text.toString()
                var endDate = endDateTextView?.text.toString()

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val start = LocalDate.parse(startDate, formatter)
                val end = LocalDate.parse(endDate, formatter)
                startDate = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                endDate = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                reservationViewModel!!.createReservation(
                    rentalWithCarWithCustomer,
                    startDate,
                    endDate,
                    viewLifecycleOwner
                )

                rentalTextView?.text = RentalState.RESERVED.toReadableString()

                val navController = findNavController()
                navController.popBackStack(R.id.create_reservation, false)
                navController.navigate(R.id.navigation_home)
            }
        }

        return view
    }

    private fun updatePrice() {
        val startDate = startDateTextView?.text.toString()
        val endDate = endDateTextView?.text.toString()

        val rentalDuration = DateHelper.getDaysBetween(startDate, endDate)

        reservationViewModel!!.rentalWithCarWithCustomer.observe(viewLifecycleOwner) { rentalWithCarWithCustomer ->
            val price = rentalWithCarWithCustomer.car?.price?.times(rentalDuration)
            priceTextView?.text = price?.toString()
        }
    }

    private fun showStartDatePicker() {
        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            startDateTextView?.text = formattedDate
            updatePrice()
        }

        datePicker.show(childFragmentManager, "startDatePicker")
    }

    private fun showEndDatePicker(startDate: LocalDate) {
        val startInstant = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(startInstant.toEpochMilli())
            .setValidator(DateValidatorPointForward.from(startInstant.toEpochMilli()))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(constraintsBuilder.build())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            endDateTextView?.text = formattedDate
            updatePrice()
        }

        datePicker.show(childFragmentManager, "endDatePicker")
    }

}