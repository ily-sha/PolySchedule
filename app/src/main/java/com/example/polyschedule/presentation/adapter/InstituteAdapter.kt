package com.example.polyschedule.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.presentation.MainActivity
import com.example.polyschedule.presentation.MainViewModel.Companion.INSTITUTE_CHOOSEN

class InstituteAdapter(private val institutes: List<List<Institute>>): RecyclerView.Adapter<InstituteAdapter.InstituteViewHolder>() {
    private lateinit var selecked: View
    class InstituteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val institute = itemView.findViewById<TextView>(R.id.institute)
        val institute2 = itemView.findViewById<TextView>(R.id.institute2)
        val institute3 = itemView.findViewById<TextView>(R.id.institute3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstituteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.institute_holder, parent, false)
        return InstituteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InstituteViewHolder, position: Int) {

        if (institutes[position].isNotEmpty()) holder.institute.text = institutes[position][0].getAbbr() else holder.institute.visibility = View.INVISIBLE
        if (institutes[position].size > 1) holder.institute2.text = institutes[position][1].getAbbr() else holder.institute2.visibility = View.INVISIBLE
        if (institutes[position].size > 2) holder.institute3.text = institutes[position][2].getAbbr() else holder.institute3.visibility = View.INVISIBLE
        holder.institute.setOnClickListener{
            it.setBackgroundResource(R.color.black)
            clicked(it, institutes[position][0])
        }
        holder.institute2.setOnClickListener{
            it.setBackgroundResource(R.color.black)
            clicked(it, institutes[position][1])
        }
        holder.institute3.setOnClickListener{
            it.setBackgroundResource(R.color.black)
            clicked(it, institutes[position][2])

        }
    }

    override fun getItemCount() = institutes.size


    fun clicked(nowButton:View, institute: Institute){
        if (this::selecked.isInitialized) { selecked.setBackgroundResource(R.color.black)}
        selecked = nowButton
        INSTITUTE_CHOOSEN = institute
//        if (MainActivity.COURSE_CHOOSEN != null && MainActivity.INSTITUTE_CHOOSEN != null) {
//            MainActivity.updateGroup()
//        }

    }
}