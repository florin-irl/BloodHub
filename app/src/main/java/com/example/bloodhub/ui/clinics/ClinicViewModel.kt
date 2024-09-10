package com.example.bloodhub.ui.clinics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bloodhub.data.local.database.AppDatabase
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.repository.ClinicRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ClinicViewModel(private val repository: ClinicRepository) : ViewModel() {
    val allClinics: LiveData<List<Clinic>> = repository
        .allClinics
        .asLiveData()

    fun insertClinic(clinic: Clinic) = viewModelScope.launch {
        repository.insertClinic(clinic)
    }
}

class ClinicViewModelFactory(private val repository: ClinicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ClinicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ClinicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}