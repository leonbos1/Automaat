package com.example.automaat.ui.inspections

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.automaat.R

class InspectionFragment : Fragment() {
    private var inspectionViewModel: InspectionViewModel? = null
    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private val PICK_IMAGE_REQUEST = 2
    private var viewDamageDescription: TextView? = null
    private var viewDamagePhoto: ImageView? = null
    private var submitButton: Button? = null
    private var uploadButton: Button? = null
    private var photo: String? = null
    private var damageDescriptionInput: EditText? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_inspection, container, false)

        viewDamageDescription = view.findViewById(R.id.viewDamageDescription)
        viewDamagePhoto = view.findViewById(R.id.viewDamagePhoto)
        submitButton = view.findViewById(R.id.submitDamageButton)
        uploadButton = view.findViewById(R.id.uploadImageButton)
        damageDescriptionInput = view.findViewById(R.id.damageDescriptionInput)

        inspectionViewModel = ViewModelProvider(this).get(InspectionViewModel::class.java)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }

        inspectionViewModel!!.inspectionWithCarWithRental =
            arguments?.getParcelable("inspectionWithCarWithRental")!!

        setViews(
            inspectionViewModel!!.inspectionWithCarWithRental.inspection?.photo.toString(),
            inspectionViewModel!!.inspectionWithCarWithRental.inspection?.result.toString()
        )

        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        submitButton?.setOnClickListener {
            val inspection = inspectionViewModel!!.inspectionWithCarWithRental.inspection
            inspection?.photo = photo.toString()
            inspection?.result = damageDescriptionInput?.text.toString()
            viewDamageDescription?.text = damageDescriptionInput?.text.toString()

            setViews(photo.toString(), damageDescriptionInput?.text.toString())

            inspectionViewModel!!.updateInspection(inspection!!)

            //findNavController().popBackStack()
        }

        return view
    }

    fun setViews(photo: String, description: String) {
        val base64Image = photo

        if (!base64Image.isNullOrEmpty()) {
            val decodedString: ByteArray =
                android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
            val decodedByte =
                android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            viewDamagePhoto?.setImageBitmap(decodedByte)
        }

        viewDamageDescription?.text = description
    }

    fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            // Use this image URI for your ImageView
            val imageView = view?.findViewById<ImageView>(R.id.viewDamagePhoto)
            imageView?.setImageURI(imageUri)
            photo = imageUri.toString()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser()
            }
            // Handle the case where permission is denied
        }
    }
}