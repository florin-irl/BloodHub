package com.example.bloodhub.ui.emergencies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodhub.R
import com.example.bloodhub.ui.models.EmergencyItemModel

class EmergenciesAdapter(private val emergencies: List<EmergencyItemModel>): RecyclerView.Adapter<EmergenciesAdapter.EmergencyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergencyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_emergency, parent, false)
        return EmergencyViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmergencyViewHolder, position: Int) {
        holder.bind(emergencies[position])
    }

    override fun getItemCount(): Int {
        return emergencies.size
    }

    inner class EmergencyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_emergency_name)
        private val location: TextView = itemView.findViewById(R.id.tv_emergency_donation_center)

        fun bind(emergency: EmergencyItemModel) {
            name.text = emergency.bloodType
            location.text = emergency.clinicName
        }
    }
}