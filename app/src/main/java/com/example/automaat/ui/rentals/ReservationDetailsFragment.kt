package com.example.automaat.ui.rentals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.automaat.databinding.FragmentReservationDetailsBinding
import com.example.automaat.viewmodels.ReservationViewModel

class ReservationDetailsFragment : Fragment() {
    private var _binding: FragmentReservationDetailsBinding? = null
    private var reservationViewModel: ReservationViewModel? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reservationViewModel = arguments?.getParcelable("reservationViewModel")

        _binding = FragmentReservationDetailsBinding.inflate(inflater, container, false)

        val root: View = binding.root


        return root
    }

}