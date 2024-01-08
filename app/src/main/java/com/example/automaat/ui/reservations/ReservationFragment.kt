package com.example.automaat.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

class ReservationFragment : Fragment() {
    private var reservationViewModel: ReservationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reservations, container, false)

        val futureAdapter = ReservationAdapter()
        val futureRecyclerView = view.findViewById<RecyclerView>(R.id.futureRecyclerView)
        futureRecyclerView.adapter = futureAdapter
        futureRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val currentAdapter = ReservationAdapter()
        val currentRecyclerView = view.findViewById<RecyclerView>(R.id.currentRecyclerView)
        currentRecyclerView.adapter = currentAdapter
        currentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val historicAdapter = ReservationAdapter()
        val historicRecyclerView = view.findViewById<RecyclerView>(R.id.historicRecyclerView)
        historicRecyclerView.adapter = historicAdapter
        historicRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        reservationViewModel!!.getFutureRentalsByCustomer(this).observe(viewLifecycleOwner) { futureRentals ->
            futureAdapter.setData(futureRentals)
        }

        // Observe current rentals
        reservationViewModel!!.getCurrentRentalsByCustomer(this).observe(viewLifecycleOwner) { currentRentals ->
            currentAdapter.setData(currentRentals)
        }

        // Observe historic rentals
        reservationViewModel!!.getHistoricRentalsByCustomer(this).observe(viewLifecycleOwner) { historicRentals ->
            historicAdapter.setData(historicRentals)
        }

        return view
    }

    fun setRentalsInLayout(
        rentalsByCustomer: List<RentalWithCarWithCustomer>,
        adapter: ReservationAdapter
    ) {
        adapter.setData(rentalsByCustomer)
    }
}