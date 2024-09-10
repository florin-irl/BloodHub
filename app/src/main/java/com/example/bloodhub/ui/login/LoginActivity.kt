package com.example.bloodhub.ui.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodhub.ui.AppActivity
import com.example.bloodhub.R
import com.example.bloodhub.helpers.extensions.logErrorMessage
import com.example.bloodhub.ui.LoadingFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        val button = findViewById<Button>(R.id.btn_login_login)
        button.setOnClickListener {
            val email = findViewById<EditText>(R.id.et_email_login).text.toString()
            val password = findViewById<EditText>(R.id.et_password_login).text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Logging in...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser

                        if (user != null) {
                            val uid = user.uid
                            firebaseFirestore.collection("users").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document != null) {
                                        val fullName = document.getString("fullName")
                                        val dateOfBirth = document.getString("dateOfBirth")
                                        val gender = document.getString("gender")
                                        val bloodType = document.getString("bloodType")

                                        val sharedPreferences =
                                            getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                        val editor = sharedPreferences.edit()
                                        editor.putString("email", email)
                                        editor.putString("password", password)
                                        editor.putString("fullName", fullName)
                                        editor.putString("dateOfBirth", dateOfBirth)
                                        editor.putString("gender", gender)
                                        editor.putString("bloodType", bloodType)
                                        editor.apply()

                                        val intent =
                                            Intent(this@LoginActivity, AppActivity::class.java)
                                        intent.putExtra("fullName", fullName)
                                        intent.putExtra("dateOfBirth", dateOfBirth)
                                        intent.putExtra("gender", gender)
                                        intent.putExtra("bloodType", bloodType)
                                        startActivity(intent)
                                        finish()

                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Invalid credentials!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                        progressDialog.dismiss()
                    } else {
                        // If login fails, check stored credentials in SharedPreferences
                        loginWithSharedPreferences(email, password)
                        progressDialog.dismiss()
                        return@addOnCompleteListener
                    }
                }
                .addOnFailureListener { exception ->
                    "Login failed: ${exception.message}".logErrorMessage()
                    exception.printStackTrace()
                    progressDialog.dismiss()
                }
        }
    }

    private fun loginWithSharedPreferences(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val storedEmail = sharedPreferences.getString("email", null)
        val storedPassword = sharedPreferences.getString("password", null)

        if (email == storedEmail && password == storedPassword) {
            // Retrieve other user details from SharedPreferences
            val fullName = sharedPreferences.getString("fullName", null)
            val dateOfBirth = sharedPreferences.getString("dateOfBirth", null)
            val gender = sharedPreferences.getString("gender", null)
            val bloodType = sharedPreferences.getString("bloodType", null)

            if (fullName != null && dateOfBirth != null && gender != null && bloodType != null) {
                val intent = Intent(this@LoginActivity, AppActivity::class.java)
                intent.putExtra("fullName", fullName)
                intent.putExtra("dateOfBirth", dateOfBirth)
                intent.putExtra("gender", gender)
                intent.putExtra("bloodType", bloodType)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
        }
    }
}