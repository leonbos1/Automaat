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

class FiltersFragment(application: Application) : Fragment() {

    private var _binding: FragmentFiltersBinding? = null

    private val binding get() = _binding!!
    private val readAllData: LiveData<List<CarModel>>
    private lateinit var carRepository: CarRepository
    private lateinit var homeAdapter: HomeAdapter
    private var availableBrands: ArrayList<String>? = null
    private var availableModels: ArrayList<String>? = null

    init {
        val userDao = AutomaatDatabase.getDatabase(application).carDao()

        carRepository = CarRepository(userDao)

        readAllData = carRepository.readAllData
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var allCars = carRepository.readAllData.value

        if (allCars == null) {
            // Temporary solution to avoid null pointer exception
            allCars = List<CarModel>(0) { i -> CarModel(0, "", "", 0, "", 0.0f, "", 0, 0, 0, "", 0, 0) }
        }

        homeAdapter = HomeAdapter(allCars, requireContext(), carRepository)

        val navController = findNavController()

        availableBrands = carRepository.getAvailableBrands()
        availableModels = carRepository.getAvailableModels()

        availableBrands?.add(0, "All")
        availableModels?.add(0, "All")

        // Set the available brands to the spinner
        binding.brandSpinner.adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                availableBrands!!
            )
        }

        // Set the available models to the spinner
        binding.modelSpinner.adapter = context?.let {
            ArrayAdapter(
                it,
                android.R.layout.simple_spinner_item,
                availableModels!!
            )
        }

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