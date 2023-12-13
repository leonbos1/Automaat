package com.example.automaat.ui.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.automaat.R
import com.example.automaat.databinding.FragmentFiltersBinding
import com.example.automaat.models.Car.CarDbHelper
import com.example.automaat.databinding.FragmentHomeBinding
import com.example.automaat.models.Car.CarsAdapter
import com.example.automaat.ui.filters.FilterViewModel

class FiltersFragment : Fragment() {

    private var _binding: FragmentFiltersBinding? = null

    private val binding get() = _binding!!
    private lateinit var dbHelper: CarDbHelper
    private lateinit var carsAdapter: CarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val filterViewModel =
            ViewModelProvider(this).get(FilterViewModel::class.java)

        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dbHelper = CarDbHelper(requireContext())
        carsAdapter = CarsAdapter(dbHelper.getAllCars(), requireContext(), dbHelper)


//        binding.root.getViewById(R.id.filterButton).setOnClickListener {
//            val filter = binding.root.getViewById(R.id) as Button
//            filter.setOnClickListener {
//                println("Filter button clicked")
//            }
//        }

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