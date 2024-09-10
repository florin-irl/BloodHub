package com.example.bloodhub.data.repository

import androidx.annotation.WorkerThread
import com.example.bloodhub.data.local.dao.EmergencyDao
import com.example.bloodhub.data.local.entity.Emergency

class EmergencyRepository(
    private val emergencyDao: EmergencyDao
) {
    val allEmergencies = emergencyDao.getEmergenciesWithClinicNames()

    @WorkerThread
    suspend fun insert(emergency: Emergency) {
        emergencyDao.insert(emergency)
    }
}