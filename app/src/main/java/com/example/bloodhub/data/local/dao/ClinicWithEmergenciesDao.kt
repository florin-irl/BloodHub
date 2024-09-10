package com.example.bloodhub.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.local.entity.ClinicWithEmergencies

@Dao
interface ClinicWithEmergenciesDao {
    @Transaction
    @Query("SELECT * FROM clinics WHERE ${Clinic.ARG_CLINIC_ID} = :clinicId")
    suspend fun getClinicWithEmergencies(clinicId: Int): ClinicWithEmergencies
}