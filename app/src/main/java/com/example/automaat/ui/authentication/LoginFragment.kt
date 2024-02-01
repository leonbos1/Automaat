package com.example.automaat.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R
import com.example.automaat.api.datamodels.Auth
import com.example.automaat.utils.SnackbarManager

class LoginFragment : Fragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val usernameInput = view.findViewById<EditText>(R.id.username)
        val passwordInput = view.findViewById<EditText>(R.id.password)
        val rememberMeCheckbox = view.findViewById<CheckBox>(R.id.rememberMe)
        val loginButton = view.findViewById<Button>(R.id.loginButton)
        val forgotPasswordLink = view.findViewById<TextView>(R.id.forgot_password)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val rememberMe = rememberMeCheckbox.isChecked
            val user = Auth(username, password, rememberMe)
            context?.let { it1 -> viewModel.authenticateUser(user, it1) }
        }
        forgotPasswordLink.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
        }

        viewModel.loginStatus.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                context?.let { SnackbarManager.showErrorSnackbar(it, "Login Failed!") }
            }
        }
    }
}