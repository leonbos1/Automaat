package com.example.automaat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.automaat.api.endpoint.Authentication
import com.example.automaat.api.endpoint.Routes
import com.example.automaat.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: CarDbHelper
    private val authentication = Authentication()

    private val TAG: String = "CHECK_RESPONSE"

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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        dbHelper = CarDbHelper(this)

        dbHelper.insertDummyCars()

        val cars = dbHelper.getAllCars()

        for (car in cars) {
            log("Car: ${car.id} ${car.brand} ${car.model}")
        }

        authentication.authenticate {
            // Get all data to local DB
            // ...
            Routes(authentication.getToken()).getAllRoutes()
        }
    }



    private fun log(message: String) {
        println("MainActivity: $message")
    }
}
