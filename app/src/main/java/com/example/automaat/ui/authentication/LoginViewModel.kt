package com.example.automaat.ui.authentication

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.automaat.api.datamodels.Auth
import com.example.automaat.api.endpoints.Account
import com.example.automaat.api.endpoints.Authentication

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    val loginStatus = MutableLiveData<Boolean>()

    fun authenticateUser(user: Auth, context: Context) {
        Authentication().authenticateUser(user, context) { success ->
            loginStatus.value = success
        }
    }

    suspend fun getAccount(context: Context) {
        Account().getAccount(context)
    }
}