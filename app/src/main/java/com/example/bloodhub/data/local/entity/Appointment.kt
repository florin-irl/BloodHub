package com.example.bloodhub.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Appointment.TABLE_NAME)
data class Appointment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ARG_APPOINTMENT_ID)
    val id: Int? = null,

    @ColumnInfo(name = ARG_APPOINTMENT_DATE)
    val date: String,

    @ColumnInfo(name = ARG_APPOINTMENT_CLINIC_ID)
    val clinicId: Int,

    @ColumnInfo(name = ARG_APPOINTMENT_USER_ID)
    val userId: String
) {
    companion object {
        const val TABLE_NAME = "appointments"
        const val ARG_APPOINTMENT_ID = "appointment_id"
        const val ARG_APPOINTMENT_DATE = "appointment_date"
        const val ARG_APPOINTMENT_CLINIC_ID = "appointment_clinic_id"
        const val ARG_APPOINTMENT_USER_ID = "appointment_user_id"
    }
}