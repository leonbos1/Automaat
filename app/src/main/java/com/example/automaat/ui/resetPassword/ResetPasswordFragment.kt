package com.example.automaat.ui.resetPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ResetPasswordFragment : Fragment() {
    private lateinit var viewModel: ResetPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ResetPasswordViewModel::class.java]

        val navigationController = findNavController()
        val resetPasswordButton = view.findViewById<Button>(R.id.reset_password_button)

        resetPasswordButton.setOnClickListener {
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val emailAddress = view.findViewById<EditText>(R.id.reset_password_email_address).text.toString()
                .toRequestBody(mediaType)
            context?.let { viewModel.resetPassword(emailAddress) }
            navigationController.navigate(R.id.action_resetPasswordFragment_to_loginFragment)
        }
    }
}