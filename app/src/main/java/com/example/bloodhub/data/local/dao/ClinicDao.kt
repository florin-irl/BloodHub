package com.example.bloodhub.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.bloodhub.data.local.entity.Clinic
import kotlinx.coroutines.flow.Flow

@Dao
interface ClinicDao {
    @Query("SELECT * FROM ${Clinic.TABLE_NAME} ORDER BY ${Clinic.ARG_CLINIC_ID} ASC")
    fun getAll(): Flow<List<Clinic>>

    @Insert
    suspend fun insert(clinic: Clinic)
}