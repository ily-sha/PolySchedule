package com.example.polyschedule.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Schedule

class ScheduleViewPagerAdapter(private val context: Context): RecyclerView.Adapter<ScheduleViewPagerAdapter.ScheduleViewHolder>() {

    var scheduleList = listOf<Schedule>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }



    inner class ScheduleViewHolder(val view: View): RecyclerView.ViewHolder(view){
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
        return if (scheduleList.isEmpty()) 0 else 6
    }

    override fun getItemViewType(position: Int): Int {
        return if (scheduleList.find {
            it.weekday - 1 == position} != null) NO_EMPTY_DAY
            else EMPTY_DAY
    }


    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        val currentSchedule = scheduleList.find { it.weekday - 1 == position }

        if (currentSchedule != null) {
            holder.recyclerView.adapter = LessonAdapter(currentSchedule.lessons)
        }
    }

    companion object {
        const val EMPTY_DAY = 10
        const val NO_EMPTY_DAY = 11
    }

}