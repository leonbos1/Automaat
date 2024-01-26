package com.example.automaat.ui.inspections

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    private var viewDamageDescription: TextView? = null
    private var viewDamagePhoto: ImageView? = null
    private var submitButton: Button? = null
    private var uploadButton: Button? = null
    private var photoButton: Button? = null
    private var photo: String? = null
    private var damageDescriptionInput: EditText? = null

    // Define ActivityResultLaunchers for camera and image picker
    private lateinit var startCamera: ActivityResultLauncher<Intent>
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ActivityResultLaunchers
        startCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageBitmap = result.data?.extras?.get("data") as Bitmap
                    viewDamagePhoto?.setImageBitmap(imageBitmap)
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray = byteArrayOutputStream.toByteArray()
                    val base64String = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    photo = base64String
                }
            }

        pickImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val imageUri = result.data?.data
                    viewDamagePhoto?.setImageURI(imageUri)
                    photo = imageUri?.let { convertImageToBase64(it) }
                }
            }

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all { it.value }
                if (granted) {
                    openCamera()
                } else {
                }
            }
    }

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
        photoButton = view.findViewById(R.id.takeImageButton)
        damageDescriptionInput = view.findViewById(R.id.damageDescriptionInput)

        inspectionViewModel = ViewModelProvider(this).get(InspectionViewModel::class.java)

        inspectionViewModel!!.inspection =
            arguments?.getParcelable("inspection")!!

        setViews(
            inspectionViewModel!!.inspection.photo ?: "",
            inspectionViewModel!!.inspection.result ?: ""
        )

        uploadButton?.setOnClickListener {
            openImageChooser()
        }

        photoButton?.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }
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

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startCamera.launch(cameraIntent)
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

    private fun openImageChooser() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
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
}