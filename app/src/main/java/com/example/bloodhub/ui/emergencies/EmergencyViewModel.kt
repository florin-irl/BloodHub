package com.example.bloodhub.ui.emergencies

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bloodhub.data.local.entity.Emergency
import com.example.bloodhub.data.local.entity.EmergencyWithClinicName
import com.example.bloodhub.data.repository.EmergencyRepository
import kotlinx.coroutines.launch

class EmergencyViewModel(private val repository: EmergencyRepository) : ViewModel() {
    val allEmergencies: LiveData<List<EmergencyWithClinicName>> =
        repository.allEmergencies.asLiveData()

    fun insertEmergency(emergency: Emergency) = viewModelScope.launch {
        repository.insert(emergency)
    }
}

class EmergencyViewModelFactory(private val repository: EmergencyRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmergencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmergencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}