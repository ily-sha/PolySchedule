package com.example.polyschedule.presentation.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Lesson
import com.example.polyschedule.domain.Schedule
import java.time.LocalDate

class ScheduleViewPagerAdapter: RecyclerView.Adapter<ScheduleViewPagerAdapter.ScheduleViewHolder>() {

    var lessonList = listOf<Schedule>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onItemSwipe: ((Int) -> Unit)? = null

    class ScheduleViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_schedule)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layout = when (viewType) {
            NO_EMPTY_DAY -> R.layout.schedule_item
            EMPTY_DAY -> R.layout.empty_day
            else -> throw RuntimeException("unknown viewType")
        }
        return ScheduleViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun getItemViewType(position: Int): Int {
        for (i in lessonList){
            if (i.weekday == position + 1) return NO_EMPTY_DAY
        }
        return EMPTY_DAY
    }


//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        println("$position, ${lessonList.size}")
//        val current = LocalDate.now().dayOfWeek.value
//        onItemSwipe?.invoke(position)
//        if (lessonList.size > current) {
        for (i in lessonList){
            if (i.weekday == position + 1) holder.recyclerView.adapter = LessonAdapter(lessonList[position].lessons)
        }



//        }
    }

    companion object {
        const val EMPTY_DAY = 10
        const val NO_EMPTY_DAY = 11
    }

}