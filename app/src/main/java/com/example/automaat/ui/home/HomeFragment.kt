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

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.readAllData.observe(viewLifecycleOwner, Observer { car ->
            adapter.setData(car)
        })

        val filterButton = view.findViewById<View>(R.id.filterButton)

        val navigationController = findNavController()

        filterButton.setOnClickListener {
            navigationController.navigate(R.id.action_navigation_home_to_filtersFragment)
        }

        return view;
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}