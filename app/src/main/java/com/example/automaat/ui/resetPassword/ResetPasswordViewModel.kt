package com.example.automaat.ui.resetPassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.automaat.api.endpoints.ResetPassword
import okhttp3.RequestBody

class ResetPasswordViewModel(application: Application) : AndroidViewModel(application) {

    fun resetPassword(emailAddress: RequestBody) {
        ResetPassword().resetPassword(emailAddress)
    }
}