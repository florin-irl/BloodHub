package com.example.bloodhub.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.bloodhub.R
import com.example.bloodhub.helpers.extensions.logErrorMessage
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class UserProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ivProfilePicture = view.findViewById<ImageView>(R.id.iv_user_profile_picture)
        val tvUserName = view.findViewById<MaterialTextView>(R.id.tv_user_profile)
        val tvDateOfBirth = view.findViewById<MaterialTextView>(R.id.tv_date_of_birth_user_profile)
        val tvGender = view.findViewById<MaterialTextView>(R.id.tv_gender)
        val tvBloodGroup = view.findViewById<MaterialTextView>(R.id.tv_blood_group)

        val signOutButton = view.findViewById<Button>(R.id.btn_logout)
        signOutButton.setOnClickListener {
            try {
                FirebaseAuth.getInstance().signOut()
            } catch (e: Exception) {
                "Failed to sign out: ${e.message}".logErrorMessage()
            }
            requireActivity().finish()
        }

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser == null) {
            val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", 0)
            tvUserName?.text = sharedPrefs.getString("fullName", "")
            tvDateOfBirth?.text = sharedPrefs.getString("dateOfBirth", "")
            tvGender?.text = sharedPrefs.getString("gender", "")
            tvBloodGroup?.text = sharedPrefs.getString("bloodType", "")
            val profilePicture = sharedPrefs.getString("profilePictureUrl", null)

            if (profilePicture != null) {
                Glide.with(requireContext()).load(profilePicture).into(ivProfilePicture!!)
            } else {
                ivProfilePicture?.setImageResource(R.drawable.account_circle)
            }

            return
        }

        val storage = FirebaseFirestore.getInstance()

        storage.collection("users").document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    tvUserName?.text = document.getString("fullName")
                    tvDateOfBirth?.text = document.getString("dateOfBirth")
                    tvGender?.text = document.getString("gender")
                    tvBloodGroup?.text = document.getString("bloodType")
                    val profilePicture = document.getString("profilePictureUrl")

                    if (profilePicture != null) {
                        Glide.with(requireContext()).load(profilePicture).into(ivProfilePicture!!)
                    } else {
                        ivProfilePicture?.setImageResource(R.drawable.account_circle)
                    }

                } else {
                    "No such document".logErrorMessage()
                }

            }
            .addOnFailureListener { exception ->
                "get failed with ".logErrorMessage()
                exception.toString().logErrorMessage()
            }
    }
}