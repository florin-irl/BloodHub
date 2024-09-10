package com.example.bloodhub.ui.appointments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.bloodhub.ApplicationController
import com.example.bloodhub.R
import com.example.bloodhub.data.local.entity.Appointment
import com.example.bloodhub.data.repository.AppointmentRepository
import com.example.bloodhub.data.repository.ClinicRepository
import com.example.bloodhub.ui.clinics.ClinicViewModel
import com.example.bloodhub.ui.clinics.ClinicViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.Locale

class NewAppointmentFragment : Fragment() {

    private val args: NewAppointmentFragmentArgs by navArgs()
    private lateinit var appointmentViewModel: AppointmentViewModel
    private lateinit var clinicViewModel: ClinicViewModel

    private lateinit var tietAppointmentDate: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_appointment, container, false)
        tietAppointmentDate = view.findViewById(R.id.tiet_appointment_date)

        val btnCancelAppointment = view.findViewById<MaterialButton>(R.id.btn_cancel_appointment)
        val btnCreateAppointment = view.findViewById<MaterialButton>(R.id.btn_save_appointment)

        tietAppointmentDate.setOnClickListener{
            showDatePickerDialog()
        }

        btnCancelAppointment.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnCreateAppointment.setOnClickListener {
            val appointmentDate = tietAppointmentDate.text.toString()

            val appointment = Appointment(
                clinicId = args.donationCenterId,
                date = tietAppointmentDate.text.toString(),
                userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

            )

            appointmentViewModel.insertAppointment(appointment)
            parentFragmentManager.popBackStack()
        }

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clinicId = args.donationCenterId

        val donationCenterTextView = view.findViewById<MaterialTextView>(R.id.til_donation_center)

        val database = ApplicationController.instance.appDatabase
        val appointmentFactory =
            AppointmentViewModelFactory(AppointmentRepository(database.appointmentDao()))
        val clinicFactory = ClinicViewModelFactory(ClinicRepository(database.clinicDao()))

        appointmentViewModel =
            ViewModelProvider(this, appointmentFactory)[AppointmentViewModel::class.java]
        clinicViewModel = ViewModelProvider(this, clinicFactory)[ClinicViewModel::class.java]

        clinicViewModel.allClinics.observe(viewLifecycleOwner) { clinics ->
            val clinic = clinics.find { it.id == clinicId }
            if (clinic != null) {
                donationCenterTextView.text = clinic.name
            }
        }


    }

    private fun showDatePickerDialogOld() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Format the selected date
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                tietAppointmentDate.setText(selectedDate)
            }, year, month, day)

        // Set the minimum date to the current date
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selectedDate ->
            // Convert the selected date to the desired format
            val calendar = android.icu.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selectedDate
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)

            tietAppointmentDate.setText(formattedDate)
        }

        picker.show(requireActivity().supportFragmentManager, picker.toString())
    }

}