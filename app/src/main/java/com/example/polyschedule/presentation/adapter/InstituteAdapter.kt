package com.example.polyschedule.presentation.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R

class InstituteAdapter(): RecyclerView.Adapter<InstituteAdapter.InstituteViewHolder>() {
    private val institutes = listOf("ФизМех", "УПКР", "ИПММ", "ИЭ", "ИППТ", "ИФНиТ", "ИПМЭиТ", "ИЭиТ", "ИДО", "ГИ", "ИЯЭ", "ИКиЗИ", "ИФКСиТ", "ИКНТ", "ИБСиБ", "ИММиТ", "ИСИ")

    var onItemClicked: ((View) -> Unit)? = null
    class InstituteViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        val tv = itemView.findViewById<TextView>(R.id.tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstituteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.enabled_item, parent, false)
        return InstituteViewHolder(itemView)
    }



    override fun onBindViewHolder(holder: InstituteViewHolder, position: Int) {
        if (position == 10){
            onItemClicked?.invoke(holder.itemView)
        }
        holder.tv.text = institutes[position]

    }

    override fun getItemCount() = institutes.size


//    fun clicked(nowButton:View, institute: Institute){
//        if (this::selecked.isInitialized) { selecked.setBackgroundResource(R.color.black)}
//        selecked = nowButton
//        INSTITUTE_CHOOSEN = institute
////        if (MainActivity.COURSE_CHOOSEN != null && MainActivity.INSTITUTE_CHOOSEN != null) {
////            MainActivity.updateGroup()
////        }
//
//    }
}