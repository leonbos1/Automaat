package com.example.automaat.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.entities.RentalModel
import com.example.automaat.entities.relations.InspectionWithCarWithRental
import com.example.automaat.entities.relations.RentalWithCarWithCustomer

class ReservationFragment : Fragment() {
    private var reservationViewModel: ReservationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reservations, container, false)

        val navigationController = findNavController()
        if (context?.let { Authentication().isUserAuthenticated(it) } == true) {
            println("User: User is authenticated")
        } else {
            println("User: User is not authenticated")
            navigationController.navigate(R.id.action_navigation_reservations_to_loginFragment)
        }
        
        val futureAdapter = ReservationAdapter { rental -> navigateToInspection(rental.rental) }
        val futureRecyclerView = view.findViewById<RecyclerView>(R.id.futureRecyclerView)
        futureRecyclerView.adapter = futureAdapter
        futureRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val currentAdapter = ReservationAdapter { rental -> navigateToInspection(rental.rental) }
        val currentRecyclerView = view.findViewById<RecyclerView>(R.id.currentRecyclerView)
        currentRecyclerView.adapter = currentAdapter
        currentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val historicAdapter = ReservationAdapter { rental -> navigateToInspection(rental.rental) }
        val historicRecyclerView = view.findViewById<RecyclerView>(R.id.historicRecyclerView)
        historicRecyclerView.adapter = historicAdapter
        historicRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        reservationViewModel = ViewModelProvider(this).get(ReservationViewModel::class.java)

        reservationViewModel!!.getFutureRentalsByCustomer()
            .observe(viewLifecycleOwner) { futureRentals ->
                futureAdapter.setData(futureRentals)
            }

        // Observe current rentals
        reservationViewModel!!.getCurrentRentalsByCustomer(this)
            .observe(viewLifecycleOwner) { currentRentals ->
                currentAdapter.setData(currentRentals)
            }

        // Observe historic rentals
        reservationViewModel!!.getHistoricRentalsByCustomer(this)
            .observe(viewLifecycleOwner) { historicRentals ->
                historicAdapter.setData(historicRentals)
            }

        return view
    }

    override fun onResume() {
        super.onResume()

        reservationViewModel?.updateRentalStateBasedOnDates()
    }

    private fun navigateToInspection(rental: RentalModel?) {
        val bundle = Bundle()

        if (rental == null) {
            return
        }

        println("rental id: ${rental.id}")

        reservationViewModel?.getInspectionByRentalId(rental.id)

        reservationViewModel?.inspection?.observe(viewLifecycleOwner) { inspection ->
            bundle.putParcelable("inspection", inspection)
            if (inspection != null) {
                findNavController().navigate(
                    R.id.action_navigation_reservations_to_inspection,
                    bundle
                )
            }
        }
    }
}
