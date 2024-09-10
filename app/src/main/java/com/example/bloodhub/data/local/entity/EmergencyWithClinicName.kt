package com.example.bloodhub.data.local.entity

data class EmergencyWithClinicName(
    val id: Int,
    val clinicId: Int,
    val clinicName: String,
    val bloodType: String
)
