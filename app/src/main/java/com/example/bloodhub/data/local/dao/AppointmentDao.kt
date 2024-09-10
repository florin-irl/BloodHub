package com.example.bloodhub.data.local.dao

import androidx.annotation.WorkerThread
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bloodhub.data.local.entity.Appointment
import com.example.bloodhub.data.local.entity.AppointmentWithClinicName
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.local.entity.Emergency
import com.example.bloodhub.data.local.entity.EmergencyWithClinicName
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM ${Appointment.TABLE_NAME} ORDER BY ${Appointment.ARG_APPOINTMENT_DATE} DESC")
    fun getAll(): Flow<List<Appointment>>

    @Query("""
        SELECT e.${Appointment.ARG_APPOINTMENT_ID} AS id, 
               e.${Appointment.ARG_APPOINTMENT_CLINIC_ID} AS clinicId, 
               c.${Clinic.ARG_CLINIC_NAME} AS clinicName, 
               e.${Appointment.ARG_APPOINTMENT_DATE} AS date,
               e.${Appointment.ARG_APPOINTMENT_USER_ID} AS userId
        FROM ${Appointment.TABLE_NAME} e 
        INNER JOIN ${Clinic.TABLE_NAME} c ON e.${Appointment.ARG_APPOINTMENT_CLINIC_ID} = c.${Clinic.ARG_CLINIC_ID}
        WHERE e.${Appointment.ARG_APPOINTMENT_USER_ID} = :userId
        ORDER BY ${Appointment.ARG_APPOINTMENT_DATE} DESC
        LIMIT 1
    """)
    fun getAppointmentByUserId(userId: String): Flow<AppointmentWithClinicName?>

    @Query("""
        SELECT e.${Appointment.ARG_APPOINTMENT_ID} AS id, 
               e.${Appointment.ARG_APPOINTMENT_CLINIC_ID} AS clinicId, 
               c.${Clinic.ARG_CLINIC_NAME} AS clinicName, 
               e.${Appointment.ARG_APPOINTMENT_DATE} AS date,
               e.${Appointment.ARG_APPOINTMENT_USER_ID} AS userId
        FROM ${Appointment.TABLE_NAME} e 
        INNER JOIN ${Clinic.TABLE_NAME} c ON e.${Appointment.ARG_APPOINTMENT_CLINIC_ID} = c.${Clinic.ARG_CLINIC_ID}
    """)
    fun getAppointmentWithClinicNames(): Flow<AppointmentWithClinicName>

    @Insert
    suspend fun insert(appointment: Appointment)
}