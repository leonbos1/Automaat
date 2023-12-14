package com.example.automaat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.automaat.R
import com.example.automaat.models.Car.CarDbHelper
import com.example.automaat.databinding.FragmentHomeBinding
import com.example.automaat.models.Car.CarsAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var dbHelper: CarDbHelper
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = CarDbHelper(requireContext())
        carsAdapter = CarsAdapter(dbHelper.getAllCars(), requireContext(), dbHelper)

        binding.carsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.carsRecyclerView.adapter = carsAdapter

        val navController = findNavController()

        //when clicked on a car_item in the list, navigate to the car details fragment
        carsAdapter.onItemClick = { car ->
            val bundle = Bundle().apply {
                putParcelable("car", car)
            }
            navController.navigate(R.id.action_navigation_home_to_car_details, bundle)
        }

        binding.filterButton.setOnClickListener {
            navController.navigate(R.id.action_navigation_home_to_filtersFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}