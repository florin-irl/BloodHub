package com.example.bloodhub.ui.appointments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.bloodhub.ApplicationController
import com.example.bloodhub.R
import com.example.bloodhub.data.repository.AppointmentRepository
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MyAppointments() : Fragment() {

    private lateinit var viewModel: AppointmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser ?: return

        val database = ApplicationController.instance.appDatabase
        val factory = AppointmentViewModelFactory(AppointmentRepository(database.appointmentDao()))

        viewModel = ViewModelProvider(this, factory)[AppointmentViewModel::class.java]

        viewModel.loadMostRecentAppointment(currentUser.uid)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_appointments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val upcomingAppointmentTime =
            view.findViewById<MaterialTextView>(R.id.tv_upcoming_appointment_time)
        val upcomingAppointmentLocation =
            view.findViewById<MaterialTextView>(R.id.tv_upcoming_appointment_location)

        viewModel.mostRecentAppointment.observe(viewLifecycleOwner) { appointment ->
            if (appointment != null) {
                upcomingAppointmentTime.text = appointment.date
                upcomingAppointmentLocation.text = appointment.clinicName
            }
        }
    }


}