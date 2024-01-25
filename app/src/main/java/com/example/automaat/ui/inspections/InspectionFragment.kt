package com.example.automaat.ui.inspections

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
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
import java.io.ByteArrayOutputStream
import java.io.InputStream

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

        inspectionViewModel!!.inspection =
            arguments?.getParcelable("inspection")!!

        setViews(
            inspectionViewModel!!.inspection.photo ?: "",
            inspectionViewModel!!.inspection.result ?: ""
        )

        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        submitButton?.setOnClickListener {
            val inspection = inspectionViewModel!!.inspection
            inspection.photo = photo.toString()
            inspection.result = damageDescriptionInput?.text.toString()
            viewDamageDescription?.text = damageDescriptionInput?.text.toString()

            setViews(photo.toString(), damageDescriptionInput?.text.toString())

            inspectionViewModel!!.updateInspection(inspection)

            //findNavController().popBackStack()
        }

        return view
    }

    fun setViews(photo: String, description: String) {
        val base64Image = photo

        if (!base64Image.isNullOrEmpty()) {
            val decodedString: ByteArray =
                Base64.decode(base64Image, Base64.DEFAULT)
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
            val imageView = view?.findViewById<ImageView>(R.id.viewDamagePhoto)

            val base64Image = imageUri?.let { convertImageToBase64(it) }

            imageView?.setImageURI(imageUri)
            photo = base64Image
        }
    }

    private fun convertImageToBase64(imageUri: Uri): String? {
        try {
            val contentResolver: ContentResolver = requireActivity().contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)

            inputStream?.let {
                val bytes = ByteArrayOutputStream()
                val buffer = ByteArray(1024)
                var len: Int

                while (it.read(buffer).also { len = it } != -1) {
                    bytes.write(buffer, 0, len)
                }

                val imageBytes: ByteArray = bytes.toByteArray()
                return Base64.encodeToString(imageBytes, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
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
        }
    }
}