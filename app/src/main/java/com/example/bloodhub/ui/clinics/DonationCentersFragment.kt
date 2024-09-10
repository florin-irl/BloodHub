package com.example.bloodhub.ui.clinics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.bloodhub.ApplicationController
import com.example.bloodhub.R
import com.example.bloodhub.data.local.entity.Clinic
import com.example.bloodhub.data.repository.ClinicRepository
import com.example.bloodhub.helpers.VolleyRequestQueue
import com.example.bloodhub.helpers.extensions.logErrorMessage
import com.example.bloodhub.ui.DonationCentersAdapter
import com.example.bloodhub.ui.models.ClinicItemModel
import com.google.gson.Gson

class DonationCentersFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_donation_centers, container, false)
}