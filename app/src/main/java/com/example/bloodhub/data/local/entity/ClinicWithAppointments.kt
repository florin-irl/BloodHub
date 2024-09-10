package com.example.bloodhub.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ClinicWithAppointments(
    @Embedded
    val clinic: Clinic,

    @Relation(
        parentColumn = Clinic.ARG_CLINIC_ID,
        entityColumn = Appointment.ARG_APPOINTMENT_CLINIC_ID
    )
    val appointments: List<Appointment>
)