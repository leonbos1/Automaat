package com.example.automaat.ui.filters

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.api.syncers.CarSyncManager
import com.example.automaat.api.syncers.CustomerSyncManager
import com.example.automaat.api.syncers.RentalSyncManager
import com.example.automaat.entities.FilterModel
import com.google.android.material.slider.Slider

class FiltersFragment() : Fragment() {
    private lateinit var deleteCarsButton: Button
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var resultsBtn: Button
    private lateinit var sortingAutoCompleteTextView: AutoCompleteTextView
    private lateinit var brandAutoCompleteTextView: AutoCompleteTextView
    private lateinit var modelAutoCompleteTextView: AutoCompleteTextView
    private lateinit var priceSlider: Slider
    private lateinit var carSyncManager: CarSyncManager
    private lateinit var rentalSyncManager: RentalSyncManager
    private lateinit var customerSyncManager: CustomerSyncManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_filters, container, false)

        val navController = findNavController()

        filterViewModel = ViewModelProvider(this).get(FilterViewModel::class.java)

        deleteCarsButton = view.findViewById(R.id.deleteCarsButton)

        deleteCarsButton.setOnClickListener {
            deleteAllData()
        }

        resultsBtn = view.findViewById(R.id.resultsButton)

        resultsBtn.setOnClickListener {
            val sorting = sortingAutoCompleteTextView.text.toString()
            val brand = brandAutoCompleteTextView.text.toString()
            val model = modelAutoCompleteTextView.text.toString()
            val price = priceSlider.value.toInt()

            println("Sorting: $sorting")

            val filterModel = FilterModel(brand, model, price, sorting)

            val bundle = Bundle()
            bundle.putParcelable("appliedFilters", filterModel)

            navController.navigate(R.id.action_navigation_filters_to_navigation_home, bundle)
        }

        carSyncManager = CarSyncManager(filterViewModel.carRepository)
        customerSyncManager = CustomerSyncManager(filterViewModel.customerRepository)

        sortingAutoCompleteTextView = view.findViewById(R.id.sortingDropdown)
        brandAutoCompleteTextView = view.findViewById(R.id.brandDropdown)
        modelAutoCompleteTextView = view.findViewById(R.id.modelDropdown)
        priceSlider = view.findViewById(R.id.priceSlider)

        setupSortingDropdown()
        setupBrandDropdown()
        setupModelDropdown()

        return view
    }

    private fun setupSortingDropdown() {
        val sortingOptions = resources.getStringArray(R.array.sorting_options)

        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, sortingOptions)
        sortingAutoCompleteTextView.setAdapter(adapter)

        sortingAutoCompleteTextView.setText("None", false)
    }

    private fun setupBrandDropdown() {
        filterViewModel.availableBrands.observe(viewLifecycleOwner) { brands ->
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, brands)
            brandAutoCompleteTextView.setAdapter(adapter)

            brandAutoCompleteTextView.setText("All", false)
        }

        modelAutoCompleteTextView.setText("All", false)

        brandAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedBrand = parent.getItemAtPosition(position) as String
            filterViewModel.loadModelsForBrand(selectedBrand)
        }
    }

    private fun setupModelDropdown() {
        filterViewModel.availableModels.observe(viewLifecycleOwner) { models ->
            val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, models)
            modelAutoCompleteTextView.setAdapter(adapter)
        }
    }

    private fun deleteAllData() {
        filterViewModel.removeAllData()

        findNavController().navigate(R.id.action_navigation_filters_to_navigation_home)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
