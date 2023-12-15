package com.example.automaat.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.databinding.FragmentFiltersBinding
import com.example.automaat.models.Car.CarDbHelper
import com.example.automaat.models.Car.FilterModel
import com.example.automaat.ui.home.HomeAdapter

class FiltersFragment : Fragment() {

    private var _binding: FragmentFiltersBinding? = null

    private val binding get() = _binding!!
    private lateinit var dbHelper: CarDbHelper
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = CarDbHelper.getInstance(requireContext())

        homeAdapter = HomeAdapter(dbHelper.getAllCars(), requireContext(), dbHelper)

        val navController = findNavController()

        binding.resultsButton.setOnClickListener {
            var brand = binding.brandEditText.text.toString()
            var model = binding.modelEditText.text.toString()

            var filterModel = FilterModel(brand, model)

            val bundle = Bundle().apply {
                putParcelable("appliedFilters", filterModel)
            }

            navController.navigate(R.id.action_navigation_filters_to_navigation_home, bundle)
        }

        binding.initCarsButton.setOnClickListener {
            dbHelper.insertDummyCars()
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