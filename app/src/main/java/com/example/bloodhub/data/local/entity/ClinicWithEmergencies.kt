package com.example.bloodhub.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ClinicWithEmergencies(
    @Embedded
    val clinic: Clinic,
    @Relation(
        parentColumn = Clinic.ARG_CLINIC_ID,
        entityColumn = Emergency.ARG_EMERGENCY_CLINIC_ID
    )
    val emergencies: List<Emergency>
)