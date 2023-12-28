package com.example.automaat.ui.cars

import androidx.fragment.app.Fragment
import com.example.automaat.databinding.FragmentCarDetailsBinding
import com.example.automaat.ui.home.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.repositories.CarRepository
import com.example.automaat.entities.CarHelper
import com.example.automaat.entities.CarModel

class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dbHelper: CarRepository
    private var car: CarModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // This retrieves the car object from the bundle
        // It was passed in from the HomeFragment at carsAdapter.onItemClick...
        car = arguments?.getParcelable("car")

        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        setCarData()

        val navController = findNavController()

        binding.reserveButton.setOnClickListener {
            val bundle = Bundle()
            
            navController.navigate(R.id.action_car_details_to_reservation_details, bundle)
        }

        return root
    }

    private fun setCarData() {
        binding.brandTextView.text = car?.brand

        val imageResourceId =
            CarHelper.getCarImageResourceId(car?.brand, car?.model, binding.carImage.context)

        if (imageResourceId != 0) {
            binding.carImage.setImageResource(imageResourceId)
        } else {
            binding.carImage.setImageResource(R.drawable.placeholder_image)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}