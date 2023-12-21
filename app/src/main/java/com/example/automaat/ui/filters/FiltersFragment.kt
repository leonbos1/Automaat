package com.example.automaat.ui.filters

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.automaat.AutomaatDatabase
import com.example.automaat.R
import com.example.automaat.databinding.FragmentFiltersBinding
import com.example.automaat.models.car.CarModel
import com.example.automaat.repositories.CarRepository
import com.example.automaat.models.car.FilterModel
import com.example.automaat.ui.home.HomeAdapter

class FiltersFragment() : Fragment() {

    private var _binding: FragmentFiltersBinding? = null

    private val binding get() = _binding!!
    private lateinit var carRepository: CarRepository
    private lateinit var homeAdapter: HomeAdapter
    private var availableBrands: ArrayList<String>? = null
    private var availableModels: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeAdapter = HomeAdapter()

        val navController = findNavController()

        binding.resultsButton.setOnClickListener {
            var brand = binding.brandSpinner.selectedItem.toString()
            var model = binding.modelSpinner.selectedItem.toString()

            var filterModel = FilterModel(brand, model)

            val bundle = Bundle().apply {
                putParcelable("appliedFilters", filterModel)
            }

            navController.navigate(R.id.action_navigation_filters_to_navigation_home, bundle)
        }

        binding.initCarsButton.setOnClickListener {
            carRepository.insertDummyCars()
        }

        binding.brandSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                availableModels = carRepository.getAvailableModelsByBrand(parent.getItemAtPosition(position).toString())
                availableModels?.add(0, "All")

                binding.modelSpinner.adapter = context?.let {
                    ArrayAdapter(
                        it,
                        android.R.layout.simple_spinner_item,
                        availableModels!!
                    )
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
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