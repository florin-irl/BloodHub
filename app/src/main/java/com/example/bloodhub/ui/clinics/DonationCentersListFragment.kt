package com.example.bloodhub.ui.clinics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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


class DonationCentersListFragment : Fragment(), OnDonationCenterClickListener {

    private val itemList = ArrayList<ClinicItemModel>()
    private val adapter = DonationCentersAdapter(itemList, this)

    private lateinit var viewModel: ClinicViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = ApplicationController.instance.appDatabase
        val factory = ClinicViewModelFactory(ClinicRepository(database.clinicDao()))
        viewModel = ViewModelProvider(this, factory)[ClinicViewModel::class.java]

        viewModel.allClinics.observe(this) { clinics ->
            if (clinics.isEmpty()) {
                // Retrieve clinics from api
                val url = "https://mocki.io/v1/a299f484-2e72-4dc3-a6cb-7e6110ea98d4"

                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response ->
                        val responseBytes = response.toByteArray(Charsets.ISO_8859_1)
                        val responseString = String(responseBytes, Charsets.UTF_8)

                        // Insert into database
                        val responseList =
                            Gson().fromJson(responseString, Array<Clinic>::class.java).toList()
                        for (clinic in responseList) {
                            viewModel.insertClinic(clinic)
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
    ): View? = inflater.inflate(R.layout.fragment_donation_centers_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpClinicList()
        getClinicItems()
    }

    private fun setUpClinicList() {
        val layoutManager = LinearLayoutManager(context)

        view?.findViewById<RecyclerView>(R.id.rv_donation_centers)?.apply {
//            setHasFixedSize(true)
            this.layoutManager = layoutManager
            this.adapter = this@DonationCentersListFragment.adapter
        }

    }

    private fun getClinicItems() {
        viewModel.allClinics.observe(viewLifecycleOwner) { clinics ->
            itemList.clear()
            itemList.addAll(clinics.map { ClinicItemModel(it.id, it.name, it.address) })
            adapter.notifyDataSetChanged()
        }
    }

    private fun goToAppointmentScreen() {
        findNavController().navigate(R.id.action_donationCentersListFragment_to_newAppointmentFragment)
    }

    override fun onDonationCenterClick(clinic: ClinicItemModel) {
        val action =
            DonationCentersListFragmentDirections.actionDonationCentersListFragmentToNewAppointmentFragment(
                clinic.id
            )
        findNavController().navigate(action)
    }

}