package com.example.bloodhub.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.local.entity.Emergency
import com.example.bloodhub.data.local.entity.EmergencyWithClinicName
import kotlinx.coroutines.flow.Flow

@Dao
interface EmergencyDao {
    @Query("SELECT * FROM ${Emergency.TABLE_NAME} ORDER BY ${Emergency.ARG_EMERGENCY_ID} ASC")
    fun getAll(): Flow<List<Emergency>>

    @Query("""
        SELECT e.${Emergency.ARG_EMERGENCY_ID} AS id, 
               e.${Emergency.ARG_EMERGENCY_CLINIC_ID} AS clinicId, 
               c.${Clinic.ARG_CLINIC_NAME} AS clinicName, 
               e.${Emergency.ARG_EMERGENCY_BLOOD_TYPE} AS bloodType 
        FROM ${Emergency.TABLE_NAME} e 
        INNER JOIN ${Clinic.TABLE_NAME} c ON e.${Emergency.ARG_EMERGENCY_CLINIC_ID} = c.${Clinic.ARG_CLINIC_ID}
    """)
    fun getEmergenciesWithClinicNames(): Flow<List<EmergencyWithClinicName>>

    @Insert
    suspend fun insert(emergency: Emergency)
}