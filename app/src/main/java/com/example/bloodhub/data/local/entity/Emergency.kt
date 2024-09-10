package com.example.bloodhub.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Emergency.TABLE_NAME)
data class Emergency(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Emergency.ARG_EMERGENCY_ID)
    val id: Int,

    @ColumnInfo(name = Emergency.ARG_EMERGENCY_CLINIC_ID)
    val clinicId: Int,

    @ColumnInfo(name = Emergency.ARG_EMERGENCY_BLOOD_TYPE)
    val bloodType: String
) {
    companion object {
        const val TABLE_NAME = "emergencies"
        const val ARG_EMERGENCY_ID = "emergency_id"
        const val ARG_EMERGENCY_CLINIC_ID = "emergency_clinic_id"
        const val ARG_EMERGENCY_BLOOD_TYPE = "emergency_blood_type"
    }
}