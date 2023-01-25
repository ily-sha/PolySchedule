package com.example.polyschedule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Speciality

class GroupAdapter(val specialities: List<Speciality>): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>() {

    class GroupViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tv = itemView.findViewById<TextView>(R.id.groupView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_holder, parent, false)
        return GroupViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.tv.text = if (!specialities[position].spec.isNullOrEmpty()) specialities[position].name
        else specialities[position].groupId


    }

    override fun getItemCount(): Int = specialities.size

}