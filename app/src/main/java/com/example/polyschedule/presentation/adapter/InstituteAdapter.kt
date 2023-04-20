package com.example.polyschedule.presentation.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.presentation.InstituteSettingFragment

class InstituteAdapter: RecyclerView.Adapter<InstituteAdapter.InstituteViewHolder>() {

    var onInstituteItemClicked: ((Institute) -> Unit)? = null
    var instituteList = mutableListOf<Institute>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var lastSelected = CourseAdapter.FIRST_CLICK

    fun clear(){
        val institute = instituteList.find { it.selected }
        institute?.let {
            instituteList.remove(it)
            instituteList.add(it.copy(id = it.id, name = it.name, abbr = it.abbr, selected = false))
            lastSelected = CourseAdapter.FIRST_CLICK
        }

    }

    class InstituteViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
        val tv = itemView.findViewById<TextView>(R.id.schedule_setting_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstituteViewHolder {
        val layout = when(viewType){
            DISABLED_INSTITUTE_ITEM -> R.layout.disabled_institute_item
            DISABLED_INSTITUTE_FIRST_ITEM -> R.layout.disabled_institute_first_item
            ENABLED_INSTITUTE_FIRST_ITEM -> R.layout.enabled_institute_first_item
            ENABLED_INSTITUTE_ITEM -> R.layout.enabled_institute_item
            else -> throw java.lang.RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return InstituteViewHolder(view)
    }



    override fun onBindViewHolder(holder: InstituteViewHolder, position: Int) {
        holder.tv.text = instituteList[position].abbr
        holder.tv.setOnClickListener {
            if (lastSelected != CourseAdapter.FIRST_CLICK) {
                extrudeLastView(lastSelected)
            }
            extrudeLastView(position)
            lastSelected = position
            onInstituteItemClicked?.invoke(instituteList[position])
        }


    }

    override fun getItemCount() = instituteList.size

    override fun getItemViewType(position: Int): Int {
        return if (!instituteList[position].selected){
            if (position < rowCount) DISABLED_INSTITUTE_FIRST_ITEM
            else DISABLED_INSTITUTE_ITEM
        } else
            if (position < rowCount) ENABLED_INSTITUTE_FIRST_ITEM
            else ENABLED_INSTITUTE_ITEM
    }

    private fun extrudeLastView(position: Int) {
        if (position >= 0){
            instituteList.add(position, instituteList[position].copy(selected = !instituteList[position].selected))
            instituteList.remove(instituteList[position + 1])
            notifyItemChanged(position)
        }
    }


    companion object {

        const val rowCount = 4
        const val ENABLED_INSTITUTE_ITEM = 100
        const val ENABLED_INSTITUTE_FIRST_ITEM = 1000
        const val DISABLED_INSTITUTE_ITEM = 101
        const val DISABLED_INSTITUTE_FIRST_ITEM = 1001

    }

}

