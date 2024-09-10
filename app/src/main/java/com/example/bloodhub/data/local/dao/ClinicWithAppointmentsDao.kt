package com.example.bloodhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bloodhub.data.local.entity.Appointment
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.local.entity.ClinicWithAppointments

@Dao
interface ClinicWithAppointmentsDao {

    @Transaction
    @Query("""SELECT * FROM ${Clinic.TABLE_NAME} 
        INNER JOIN ${Appointment.TABLE_NAME} ON ${Clinic.TABLE_NAME}.${Clinic.ARG_CLINIC_ID} = ${Appointment.TABLE_NAME}.${Appointment.ARG_APPOINTMENT_CLINIC_ID} 
        WHERE ${Appointment.TABLE_NAME}.${Appointment.ARG_APPOINTMENT_USER_ID} = :userId""")
    suspend fun getClinicWithAppointmentsByUserId(userId: Int): ClinicWithAppointments
}