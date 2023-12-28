package com.example.automaat.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.api.datamodels.Rental
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.endpoints.Cars
import com.example.automaat.api.endpoints.Rentals
import com.example.automaat.entities.FilterModel
import com.example.automaat.ui.home.HomeAdapter

class FiltersFragment() : Fragment() {
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var initCarsButton: Button
    private lateinit var deleteCarsButton: Button
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var resultsBtn: Button
    private lateinit var brandSpinner: Spinner
    private lateinit var modelSpinner: Spinner
    private lateinit var getAllCarsButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)

        val navController = findNavController()

        filterViewModel = ViewModelProvider(this).get(FilterViewModel::class.java)

        initCarsButton = view.findViewById(R.id.initCarsButton)

        initCarsButton.setOnClickListener {
            initDummyCars()
        }

        deleteCarsButton = view.findViewById(R.id.deleteCarsButton)

        deleteCarsButton.setOnClickListener {
            deleteAllCars()
        }

        resultsBtn = view.findViewById(R.id.resultsButton)

        resultsBtn.setOnClickListener {
            val brand = brandSpinner.selectedItem.toString()
            val model = modelSpinner.selectedItem.toString()

            val filterModel = FilterModel(brand, model)

            val bundle = Bundle()
            bundle.putParcelable("appliedFilters", filterModel)

            navController.navigate(R.id.action_navigation_filters_to_navigation_home, bundle)
        }

        getAllCarsButton = view.findViewById(R.id.getAllCarsButton)

        getAllCarsButton.setOnClickListener {
            Authentication().authenticate {
                Cars(filterViewModel.carRepository).getAllCars()
                Rentals(filterViewModel.rentalRepository).getAllRentals()
            }
        }

        brandSpinner = view.findViewById(R.id.brandSpinner)

        modelSpinner = view.findViewById(R.id.modelSpinner)

        setupBrandSpinner()

        setupModelSpinner()

        return view
    }

    private fun setupBrandSpinner() {
        filterViewModel.availableBrands.observe(viewLifecycleOwner) { brands ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, brands)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            brandSpinner.adapter = adapter
        }

        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedBrand = parent.getItemAtPosition(position) as String
                filterViewModel.loadModelsForBrand(selectedBrand)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case where no brand selection is made
            }
        }
    }

    private fun setupModelSpinner() {
        filterViewModel.availableModels.observe(viewLifecycleOwner) { models ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, models)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            modelSpinner.adapter = adapter
        }
    }

    //TODO: Remove this function
    private fun initDummyCars() {
        filterViewModel.insertDummyCars()

        findNavController().navigate(R.id.action_navigation_filters_to_navigation_home)
    }

    private fun deleteAllCars() {
        filterViewModel.removeAllCars()

        findNavController().navigate(R.id.action_navigation_filters_to_navigation_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}