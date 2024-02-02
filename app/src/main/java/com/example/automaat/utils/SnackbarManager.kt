package com.example.automaat.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.automaat.MainActivity
import com.example.automaat.R
import com.google.android.material.snackbar.Snackbar

object SnackbarManager {

    fun showSuccessSnackbar(context: Context, text: String) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
            snackbar.show()
        }
    }

    fun showErrorSnackbar(context: Context, text: String) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
            snackbar.show()
        }
    }

    fun showOnlineSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "Online", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
            snackbar.show()
        }
    }

    fun showOfflineSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "Offline", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
            snackbar.show()
        }
    }

    fun showRentalAlreadyReservedSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "This car is already reserved", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
            snackbar.show()
        }
    }

    fun showRentalReservedSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "Car reserved succesfully", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
            snackbar.show()
        }
    }

    private fun getView(): View? {
        return MainActivity.getRootView()
    }
}

