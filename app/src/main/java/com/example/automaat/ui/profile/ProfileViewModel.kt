package com.example.automaat.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.automaat.api.endpoints.Account
import org.json.JSONObject

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    fun getAccountData(): JSONObject? {
        return Account().getSavedAccountData(getApplication<Application>().applicationContext)
    }
}