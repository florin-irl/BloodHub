package com.example.bloodhub.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodhub.R
import com.example.bloodhub.ui.clinics.OnDonationCenterClickListener
import com.example.bloodhub.ui.models.ClinicItemModel
import kotlin.reflect.KFunction0

class DonationCentersAdapter(private val donationCenters: List<ClinicItemModel>,private val listener: OnDonationCenterClickListener):
    RecyclerView.Adapter<DonationCentersAdapter.DonationCenterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationCenterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_donation_center, parent, false)
        return DonationCenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationCenterViewHolder, position: Int) {
        holder.bind(donationCenters[position])
    }

    override fun getItemCount(): Int {
        return donationCenters.size
    }

    inner class DonationCenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_donation_center_name)
        private val address: TextView = itemView.findViewById(R.id.tv_donation_center_address)


        fun bind(clinic: ClinicItemModel) {
            name.text = clinic.name
            address.text = clinic.address
            itemView.setOnClickListener {
                listener.onDonationCenterClick(clinic)
            }
        }


    }


}