package com.example.automaat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.models.car.FilterModel

class HomeFragment() : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        val adapter = HomeAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.carsRecyclerView)

        val filterModel = arguments?.getParcelable<FilterModel>("appliedFilters")

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
            setCars(filterModel, adapter)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val filterButton = view.findViewById<View>(R.id.filterButton)

        val navigationController = findNavController()

        filterButton.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_home_to_filtersFragment)
        }

        adapter.onItemClick = { car ->
            val bundle = Bundle()
            bundle.putParcelable("car", car)
            navigationController.navigate(R.id.action_navigation_home_to_car_details, bundle)
        }

        return view;
    }

    fun setCars(filterModel: FilterModel?, adapter: HomeAdapter) {
        if (filterModel == null) {
            homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
                adapter.setData(car)
            })
        }

        else if (filterModel.brand == "All" && filterModel.model == "All") {
            homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
                adapter.setData(car)
            })
        }

        else if (filterModel.brand == "All") {
            homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
                val filteredCars = car.filter { it.model == filterModel.model }
                adapter.setData(filteredCars)
            })
        }

        else if (filterModel.model == "All") {
            homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
                val filteredCars = car.filter { it.brand == filterModel.brand }
                adapter.setData(filteredCars)
            })
        }

        else {
            homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
                val filteredCars = car.filter { it.brand == filterModel.brand && it.model == filterModel.model }
                adapter.setData(filteredCars)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}