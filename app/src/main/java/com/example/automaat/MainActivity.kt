package com.example.automaat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authentication = Authentication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_reservations, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp() || super.onSupportNavigateUp()

        //Unreachable code
//        authentication.authenticate {
//            // Get all data and store in local DB
//            // ...
//            Routes().getAllRoutes()
//            Routes().getRouteById(1)
//
//            Cars().getAllCars()
//            Cars().getCarById(1)
//
//            Customers().getAllCustomers()
//            Customers().getCustomerById(1)
//
//            Rentals().getAllRentals()
//            Rentals().getRentalById(1)
//
//            Inspections().getAllInspections()
//            Inspections().getInspectionById(1)
//
//            InspectionPhotos().getAllInspectionPhotos()
//            InspectionPhotos().getInspectionPhotoById(1)
//        }
    }

    private fun log(message: String) {
        println("MainActivity: $message")
    }
}
