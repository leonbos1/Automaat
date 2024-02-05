package com.example.automaat

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.automaat.databinding.ActivityMainBinding
import com.example.automaat.utils.NetworkMonitor
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var networkMonitor: NetworkMonitor

    companion object {
        const val REQUEST_CODE_POST_NOTIFICATIONS = 1
        private var rootView: View? = null
        fun getRootView(): View? = rootView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rootView = binding.root

        networkMonitor = NetworkMonitor(this)
        networkMonitor.startNetworkCallback()

        val navView: BottomNavigationView = binding.navView

        val navHostManager = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostManager.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_reservations, R.id.profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        getSharedPreferences("userToken", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "id_token") {
            invalidateOptionsMenu()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val userTokenPrefs = getSharedPreferences("userToken", Context.MODE_PRIVATE)
                userTokenPrefs.edit().clear().apply()

                val accountResponsePrefs = getSharedPreferences("AccountResponse", Context.MODE_PRIVATE)
                accountResponsePrefs.edit().clear().apply()

                navController.navigate(R.id.action_global_to_loginFragment)

                invalidateOptionsMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_action_menu, menu)
        val logoutMenuItem = menu?.findItem(R.id.action_logout)
        val sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE)
        val tokenExists = sharedPreferences.contains("id_token")
        if (logoutMenuItem != null) {
            logoutMenuItem.isVisible = tokenExists
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        getSharedPreferences("userToken", Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this)
        networkMonitor.stopNetworkCallback()
    }
}
