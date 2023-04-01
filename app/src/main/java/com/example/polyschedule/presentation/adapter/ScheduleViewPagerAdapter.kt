package com.example.polyschedule.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Schedule

class ScheduleViewPagerAdapter(private val context: Context): RecyclerView.Adapter<ScheduleViewPagerAdapter.ScheduleViewHolder>() {

    var scheduleList: MutableList<out Schedule?> = MutableList(8) { null }
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(view: View): RecyclerView.ViewHolder(view){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layout = when (viewType) {
            NO_EMPTY_DAY -> R.layout.schedule_item
            EMPTY_DAY -> R.layout.empty_day
            else -> throw RuntimeException("unknown viewType in ScheduleViewPagerAdapter")
        }
        return ScheduleViewHolder(LayoutInflater.from(context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return scheduleList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (scheduleList[position]?.lessonsMap?.isEmpty() != false) EMPTY_DAY
            else NO_EMPTY_DAY
    }


    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
//        Log.d("MainTr", "onBindViewHolder $position")
        val currentSchedule = scheduleList[position]
        if (currentSchedule != null){
            if (currentSchedule.lessonsMap.isNotEmpty()) {
                LessonAdapter(context).apply {
                    this.lessonList = currentSchedule.lessonsMap.toMutableMap()
                    holder.recyclerView.adapter = this
                }
            }
        }

    }

    companion object {
        const val EMPTY_DAY = 10
        const val NO_EMPTY_DAY = 11
    }

}