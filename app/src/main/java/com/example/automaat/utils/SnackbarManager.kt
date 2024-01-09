package com.example.automaat.utils

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.automaat.MainActivity
import com.example.automaat.R
import com.google.android.material.snackbar.Snackbar

object SnackbarManager {

    fun showOnlineSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "You are online", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.green))
            snackbar.show()
        }
    }

    fun showOfflineSnackbar(context: Context) {
        getView()?.let { view ->
            val snackbar = Snackbar.make(view, "You are offline", Snackbar.LENGTH_LONG)
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red))
            snackbar.show()
        }
    }

    private fun getView(): View? {
        return MainActivity.getRootView()
    }
}

