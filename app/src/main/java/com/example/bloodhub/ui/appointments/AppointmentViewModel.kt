package com.example.bloodhub.ui.appointments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bloodhub.data.local.entity.Appointment
import com.example.bloodhub.data.local.entity.AppointmentWithClinicName
import com.example.bloodhub.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class AppointmentViewModel(private val repository: AppointmentRepository) : ViewModel() {

    private val _mostRecentAppointment = MutableLiveData<AppointmentWithClinicName?>()
    val mostRecentAppointment: LiveData<AppointmentWithClinicName?> get() = _mostRecentAppointment

    fun loadMostRecentAppointment(userId: String) {
        viewModelScope.launch {
            repository.getMostRecentAppointmentByUserId(userId)
                .collect { appointment ->
                    _mostRecentAppointment.value = appointment
                }
        }
    }

    fun insertAppointment(appointment: Appointment) = viewModelScope.launch {
        repository.insertAppointment(appointment)
    }
}

class AppointmentViewModelFactory(private val repository: AppointmentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppointmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}