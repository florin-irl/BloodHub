package com.example.bloodhub.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = Clinic.TABLE_NAME)
data class Clinic(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Clinic.ARG_CLINIC_ID)
    val id: Int,

    @ColumnInfo(name = Clinic.ARG_CLINIC_NAME)
    val name: String,

    @ColumnInfo(name = Clinic.ARG_CLINIC_ADDRESS)
    val address: String,
) {
    companion object {
        const val TABLE_NAME = "clinics"
        const val ARG_CLINIC_ID = "clinic_id"
        const val ARG_CLINIC_NAME = "clinic_name"
        const val ARG_CLINIC_ADDRESS = "clinic_address"
    }
}