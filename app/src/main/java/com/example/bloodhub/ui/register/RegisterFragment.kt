package com.example.bloodhub.ui.register

import android.app.ProgressDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bloodhub.R
import com.example.bloodhub.helpers.extensions.logErrorMessage
import com.example.bloodhub.ui.LoadingFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Locale

class RegisterFragment : Fragment() {

    private lateinit var editText: EditText
    private lateinit var autoCompleteGender: AutoCompleteTextView
    private lateinit var autoCompleteBloodGroup: AutoCompleteTextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseStorage: FirebaseStorage

    private var userID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var profilePictureUri : Uri? = null
        val profilePicture = view.findViewById<ShapeableImageView>(R.id.iv_user_profile_picture)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    profilePictureUri = it
                    profilePicture.setImageURI(it)
                    Toast.makeText(
                        requireContext(),
                        "Profile picture selected",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } ?: run {
                    Toast.makeText(
                        requireContext(),
                        "No profile picture selected",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        val pickPictureButton = view.findViewById<Button>(R.id.btn_pick_profile_picture)
        pickPictureButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        val registerButton = view.findViewById<Button>(R.id.btn_register_form)

        registerButton.setOnClickListener {
            registerUser(view, profilePictureUri)
        }
    }

    private fun registerUser(view: View, profilePictureUri: Uri? = null) {
        val email = view.findViewById<TextView>(R.id.et_email).text.toString()
        val password = view.findViewById<TextView>(R.id.et_password).text.toString()
        val fullName = view.findViewById<TextView>(R.id.et_full_name).text.toString()
        val dateOfBirth = view.findViewById<TextView>(R.id.et_date_of_birth).text.toString()
        val bloodType = autoCompleteBloodGroup.text.toString()
        val gender = autoCompleteGender.text.toString()

        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty() || dateOfBirth.isEmpty() || bloodType.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setMessage("Registering...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userID = firebaseAuth.currentUser?.uid.toString()
                    profilePictureUri?.let {
                        uploadProfilePicture(it, userID, fullName, email, dateOfBirth, gender, bloodType)
                    } ?: run {
                        saveUserToFirestore(userID, fullName, email, dateOfBirth, gender, bloodType, null)
                    }
                } else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                }
                progressDialog.dismiss()
            }
    }

    private fun uploadProfilePicture(
        uri: Uri,
        userID: String,
        fullName: String,
        email: String,
        dateOfBirth: String,
        gender: String,
        bloodType: String
    ) {
        val storageReference = firebaseStorage.reference.child("profilePictures/$userID.jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveUserToFirestore(
                        userID,
                        fullName,
                        email,
                        dateOfBirth,
                        gender,
                        bloodType,
                        downloadUri.toString()
                    )
                }.addOnFailureListener { e ->
                    "Error getting download URL".logErrorMessage()
                    e.toString().logErrorMessage()
                    Toast.makeText(
                        requireContext(),
                        "Profile picture upload failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                "Error uploading profile picture".logErrorMessage()
                e.toString().logErrorMessage()
                Toast.makeText(
                    requireContext(),
                    "Profile picture upload failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun saveUserToFirestore(
        userID: String,
        fullName: String,
        email: String,
        dateOfBirth: String,
        gender: String,
        bloodType: String,
        profilePictureUrl: String?
    ) {
        val user = hashMapOf(
            "fullName" to fullName,
            "email" to email,
            "dateOfBirth" to dateOfBirth,
            "gender" to gender,
            "bloodType" to bloodType,
            "profilePictureUrl" to profilePictureUrl
        )
        firebaseFirestore.collection("users").document(userID)
            .set(user)
            .addOnSuccessListener {
                "DocumentSnapshot successfully written!".logErrorMessage()
                goToRegisterSuccess()
            }
            .addOnFailureListener { e ->
                "Error writing document".logErrorMessage()
                e.toString().logErrorMessage()
                Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        autoCompleteGender = view.findViewById(R.id.spinner_gender)
        autoCompleteBloodGroup = view.findViewById(R.id.spinner_blood_group)

        val genderOptions = arrayOf("Male", "Female")
        val genderAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            genderOptions
        )
        autoCompleteGender.setAdapter(genderAdapter)

        val bloodGroupOptions = arrayOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        val bloodGroupAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            bloodGroupOptions
        )
        autoCompleteBloodGroup.setAdapter(bloodGroupAdapter)

        autoCompleteGender.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                autoCompleteGender.showDropDown()
            }
        }
        autoCompleteGender.setOnClickListener {
            autoCompleteGender.showDropDown()
        }
        autoCompleteBloodGroup.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    autoCompleteBloodGroup.showDropDown()
                }
            }
        autoCompleteBloodGroup.setOnClickListener {
            autoCompleteBloodGroup.showDropDown()
        }

        editText = view.findViewById(R.id.et_date_of_birth)
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }
        editText.setOnClickListener {
            showDatePickerDialog()
        }
        return view
    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selectedDate ->
            // Convert the selected date to the desired format
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selectedDate
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            editText.setText(formattedDate)
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

    private fun goToRegisterSuccess() {
        val action =
            RegisterFragmentDirections.actionRegisterFragmentToRegisterSuccessFragment2()
        findNavController().navigate(action)
    }
}