package com.example.automaat.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automaat.R
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.entities.FilterModel
import com.example.automaat.entities.RentalState
import com.example.automaat.entities.relations.CarWithRental

class HomeFragment() : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var swipeContainer: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val adapter = HomeAdapter()

        val recyclerView = view.findViewById<RecyclerView>(R.id.carsRecyclerView)

        val filterModel = arguments?.getParcelable<FilterModel>("appliedFilters")

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.carsWithRentals.observe(viewLifecycleOwner, Observer { car ->
            setCars(filterModel, adapter)
        })

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val filterButton = view.findViewById<View>(R.id.filterButton)

        val navigationController = findNavController()


        if (context?.let { Authentication().isUserAuthenticated(it) } == true) {
            println("User: User is authenticated")
        } else {
            println("User: User is not authenticated")
            navigationController.navigate(R.id.action_homeFragment_to_loginFragment)
        }

        filterButton.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_home_to_filtersFragment)
        }

        swipeContainer = view.findViewById(R.id.swipeRefreshLayout)

        swipeContainer.setOnRefreshListener {
            homeViewModel.refreshCars(requireContext())
            swipeContainer.isRefreshing = false
        }

        adapter.onItemClick = { car ->
            val bundle = Bundle()

            homeViewModel.carsWithRentals.observe(viewLifecycleOwner) {
                val foundCar =
                    homeViewModel.carsWithRentals.value?.find { it.car.id == car.id }

                bundle.putParcelable("car", foundCar)
                navigationController.navigate(R.id.action_navigation_home_to_car_details, bundle)
            }
        }

        val filterContainer = view.findViewById<LinearLayout>(R.id.filtersContainer)

        addFilterButtons(filterModel, filterContainer)

        return view;
    }

    fun addFilterButtons(filterModel: FilterModel?, filterContainer: LinearLayout) {
        if (filterModel == null) {
            return
        }

        if (filterModel.brand != "All") {
            val brandButton = Button(requireContext())
            brandButton.text = filterModel.brand
            brandButton.setOnClickListener {
                val newFilterModel =
                    FilterModel("All", filterModel.model, filterModel.price, filterModel.sorting)
                val bundle = Bundle()
                bundle.putParcelable("appliedFilters", newFilterModel)
                findNavController().navigate(R.id.action_navigation_home_to_home, bundle)
            }
            filterContainer.addView(brandButton)
        }

        if (filterModel.model != "All") {
            val modelButton = Button(requireContext())
            modelButton.text = filterModel.model
            modelButton.setOnClickListener {
                val newFilterModel =
                    FilterModel(filterModel.brand, "All", filterModel.price, filterModel.sorting)
                val bundle = Bundle()
                bundle.putParcelable("appliedFilters", newFilterModel)
                findNavController().navigate(R.id.action_navigation_home_to_home, bundle)
            }
            filterContainer.addView(modelButton)
        }

        if (filterModel.price != 0) {
            val priceButton = Button(requireContext())
            priceButton.text = "€${filterModel.price}"
            priceButton.setOnClickListener {
                val newFilterModel =
                    FilterModel(filterModel.brand, filterModel.model, 0, filterModel.sorting)
                val bundle = Bundle()
                bundle.putParcelable("appliedFilters", newFilterModel)
                findNavController().navigate(R.id.action_navigation_home_to_home, bundle)
            }
            filterContainer.addView(priceButton)
        }

        if (filterModel.sorting != "None") {
            val sortingButton = Button(requireContext())
            sortingButton.text = filterModel.sorting
            sortingButton.setOnClickListener {
                val newFilterModel =
                    FilterModel(filterModel.brand, filterModel.model, filterModel.price, "None")
                val bundle = Bundle()
                bundle.putParcelable("appliedFilters", newFilterModel)
                findNavController().navigate(R.id.action_navigation_home_to_home, bundle)
            }
            filterContainer.addView(sortingButton)
        }
    }

    fun applySorting(
        filterModel: FilterModel?,
        availableCars: List<CarWithRental>?
    ): List<CarWithRental>? {
        if (filterModel == null) {
            return availableCars
        }

        if (filterModel.sorting == "None") {
            return availableCars
        } else if (filterModel.sorting == "Alphabetical") {
            return availableCars?.sortedBy { it.car.brand }
        } else if (filterModel.sorting == "Alphabetical (reversed)") {
            return availableCars?.sortedByDescending { it.car.brand }
        } else if (filterModel.sorting == "Price (low to high)") {
            return availableCars?.sortedBy { it.car.price }
        } else if (filterModel.sorting == "Price (high to low)") {
            return availableCars?.sortedByDescending { it.car.price }
        }

        return availableCars
    }

    fun setCars(filterModel: FilterModel?, adapter: HomeAdapter) {
        var availableCars =
            homeViewModel.carsWithRentals.value?.filter { it.rental?.state == RentalState.RETURNED || it.rental?.state == null || it.rental.customerId == null }

        availableCars = applySorting(filterModel, availableCars)

        if (filterModel?.price != null && filterModel.price!! > 0) {
            availableCars = availableCars?.filter { it.car.price <= (filterModel.price ?: 10000) }
        }

        if (filterModel == null) {
            adapter.setData(availableCars ?: emptyList())
        } else if (filterModel.brand == "All" && filterModel.model == "All") {
            adapter.setData(availableCars ?: emptyList())
        } else if (filterModel.brand == "All") {
            val filteredCars =
                availableCars?.filter { it.car.model == filterModel.model && carIsAvailable(it) }
                    ?: emptyList()
            adapter.setData(filteredCars)
        } else if (filterModel.model == "All") {
            val filteredCars =
                availableCars?.filter { it.car.brand == filterModel.brand && carIsAvailable(it) }
                    ?: emptyList()
            adapter.setData(filteredCars)
        } else {
            val filteredCars = availableCars?.filter {
                it.car.brand == filterModel.brand && it.car.model == filterModel.model
            } ?: emptyList()
            adapter.setData(filteredCars)
        }
    }

    private fun carIsAvailable(car: CarWithRental): Boolean {
        return (car.rental?.state == null || car.rental.customerId == null) && (car.rental?.state == RentalState.RETURNED || car.rental?.state == null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
