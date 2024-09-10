package com.example.bloodhub.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bloodhub.ApplicationController
import com.example.bloodhub.R
import com.example.bloodhub.data.local.database.AppDatabase
import com.example.bloodhub.data.local.entity.Appointment
import com.example.bloodhub.data.repository.AppointmentRepository
import com.example.bloodhub.ui.login.LoginActivity
import com.example.bloodhub.ui.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val intent = Intent(this, AppActivity::class.java)
            startActivity(intent)
        }

        val loginButton: Button = findViewById(R.id.btn_login)
        loginButton.setOnClickListener {
            goToLogin()
        }

        val registerButton: Button = findViewById(R.id.btn_register)
        registerButton.setOnClickListener {
            goToRegister()
        }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun goToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}