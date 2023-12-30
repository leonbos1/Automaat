package com.example.automaat.ui.cars

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import com.example.automaat.databinding.FragmentCarDetailsBinding
import com.example.automaat.ui.home.HomeViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.repositories.CarRepository
import com.example.automaat.entities.CarHelper
import com.example.automaat.entities.CarModel
import com.example.automaat.entities.toReadableString

class CarDetailsFragment : Fragment() {
    private var car: CarModel? = null
    private lateinit var reserveButton: Button
    private lateinit var carImage: ImageView
    private lateinit var brandTextView: TextView
    private lateinit var modelTextView: TextView
    private lateinit var engineTextView: TextView
    private lateinit var optionsTextView: TextView
    private lateinit var engineContentTextView: TextView
    private lateinit var firstRegistrationTextView: TextView
    private lateinit var fuelTextView: TextView
    private lateinit var availableSinceTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_car_details, container, false)

        car = arguments?.getParcelable("car")

        brandTextView = view.findViewById(R.id.brandTextView)
        modelTextView = view.findViewById(R.id.modelTextView)
        engineTextView = view.findViewById(R.id.engineTypeTextView)
        carImage = view.findViewById(R.id.carImage)
        optionsTextView = view.findViewById(R.id.optionsTextView)
        engineContentTextView = view.findViewById(R.id.engineContentTextView)
        firstRegistrationTextView = view.findViewById(R.id.modelYearContentTextView)
        fuelTextView = view.findViewById(R.id.fuelContentTextView)
        availableSinceTextView = view.findViewById(R.id.availableSinceContentTextView)

        setCarData()

        val navController = findNavController()

        reserveButton = view.findViewById(R.id.reserveButton)

        reserveButton.setOnClickListener {
            val bundle = Bundle()
            
            navController.navigate(R.id.action_car_details_to_reservation_details, bundle)
        }

        return view
    }

    private fun setCarData() {
        brandTextView.text = car?.brand
        modelTextView.text = car?.model
        engineTextView.text = car?.engineSize.toString() + " Liter  Engine"
        optionsTextView.text = car?.options
        engineContentTextView.text = car?.engineSize.toString() + " Liter  Engine"
        firstRegistrationTextView.text = car?.modelYear.toString()
        fuelTextView.text = car?.fuelType?.toReadableString()
        availableSinceTextView.text = car?.since

        val imageResourceId =
            CarHelper.getCarImageResourceId(car?.brand, car?.model, carImage.context)

        if (imageResourceId != 0) {
            carImage.setImageResource(imageResourceId)
        } else {
            carImage.setImageResource(R.drawable.placeholder_image)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}