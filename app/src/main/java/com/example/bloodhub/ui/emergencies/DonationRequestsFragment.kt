package com.example.bloodhub.ui.emergencies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.example.bloodhub.ApplicationController
import com.example.bloodhub.R
import com.example.bloodhub.data.local.entity.Emergency
import com.example.bloodhub.data.repository.EmergencyRepository
import com.example.bloodhub.helpers.VolleyRequestQueue
import com.example.bloodhub.helpers.extensions.logErrorMessage
import com.example.bloodhub.ui.models.EmergencyItemModel
import com.google.gson.Gson

class DonationRequestsFragment : Fragment() {

    private val itemList = ArrayList<EmergencyItemModel>()
    private val adapter = EmergenciesAdapter(itemList)

    private lateinit var viewModel: EmergencyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ApplicationController.instance.appDatabase
        val factory = EmergencyViewModelFactory(EmergencyRepository(database.emergencyDao()))
        viewModel = ViewModelProvider(this, factory)[EmergencyViewModel::class.java]

        viewModel.allEmergencies.observe(this) { emergencies ->
            if (emergencies.isEmpty()) {
                // Retrieve emergencies from api
                val url = "https://mocki.io/v1/105376f4-efef-4a70-9a6a-35ca5bf4b361"

                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        // Insert into database
                        val responseList =
                            Gson().fromJson(response, Array<Emergency>::class.java).toList()

                        for (emergency in responseList) {
                            viewModel.insertEmergency(emergency)
                        }
                        adapter.notifyDataSetChanged()
                    },
                    { error ->
                        // Show error message
                        "Error: $error".logErrorMessage()
                    }
                )
                VolleyRequestQueue.addToRequestQueue(stringRequest)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_donation_requests, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpEmergencyList()
        getEmergencyItems()
    }

    private fun setUpEmergencyList() {
        val layoutManager = LinearLayoutManager(context)

        view?.findViewById<RecyclerView>(R.id.rv_emergencies)?.apply {

            this.layoutManager = layoutManager
            this.adapter = this@DonationRequestsFragment.adapter
        }
    }

    private fun getEmergencyItems() {
        viewModel.allEmergencies.observe(viewLifecycleOwner) { emergencies ->
            itemList.clear()
            itemList.addAll(emergencies.map {
                EmergencyItemModel(
                    it.id,
                    it.clinicName,
                    it.bloodType
                )
            })
            adapter.notifyDataSetChanged()
        }
    }
}