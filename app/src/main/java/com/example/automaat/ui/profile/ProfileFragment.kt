package com.example.automaat.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.automaat.R
import com.example.automaat.api.endpoints.Authentication
import com.example.automaat.utils.SnackbarManager

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val navigationController = findNavController()


        if (context?.let { Authentication().isUserAuthenticated(it) } == true) {
            println("User: User is authenticated")
        } else {
            println("User: User is not authenticated")
            navigationController.navigate(R.id.action_profile_to_loginFragment)
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val textViewWelcome = view.findViewById<TextView>(R.id.textViewWelcome)
        val imageViewProfile = view.findViewById<ImageView>(R.id.imageViewProfile)
        val textViewFirstName = view.findViewById<TextView>(R.id.textViewFirstName)
        val textViewLastName = view.findViewById<TextView>(R.id.textViewLastName)
        val textViewEmail = view.findViewById<TextView>(R.id.textViewEmail)

        val accountData = viewModel.getAccountData()
        if (accountData != null) {
//            val imageUrl = accountData.optString("imageUrl", "")
            val imageUrl = "https://media.licdn.com/dms/image/D4E03AQG1gd3H6VB5mg/profile-displayphoto-shrink_200_200/0/1700856951206?e=1712793600&v=beta&t=orkhs0k7uLWeAbRlQkHBP3tzG8LKnatHU-xqDckBppM"
            println("ImageURL = $imageUrl")
            Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_profile_template_black)
                .into(imageViewProfile)

            val fullName = "${accountData.getString("firstName")} ${accountData.getString("lastName")}"
            val welcomeText = "Welcome, $fullName"
            textViewWelcome.text = welcomeText

            textViewFirstName.text = accountData.getString("firstName")
            textViewLastName.text = accountData.getString("lastName")
            textViewEmail.text = accountData.getString("email")

        } else {
            context?.let { SnackbarManager.showErrorSnackbar(it, "Loading profile failed...!") }
        }
    }

}