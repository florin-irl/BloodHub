package com.example.bloodhub.data.local.entity

data class AppointmentWithClinicName(
    val id: Int,
    val date: String,
    val clinicId: Int,
    val userId: String,
    val clinicName: String
) {
}