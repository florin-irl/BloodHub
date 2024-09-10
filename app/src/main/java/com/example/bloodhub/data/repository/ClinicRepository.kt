package com.example.bloodhub.data.repository

import androidx.annotation.WorkerThread
import com.example.bloodhub.data.local.dao.ClinicDao
import com.example.bloodhub.data.local.entity.Clinic
import kotlinx.coroutines.flow.Flow

class ClinicRepository(
    private val clinicDao: ClinicDao
) {
    val allClinics : Flow<List<Clinic>> = clinicDao.getAll()

    @WorkerThread
    suspend fun insertClinic(clinic: Clinic) {
        clinicDao.insert(clinic)
    }
}