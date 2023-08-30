package com.example.polyschedule.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.polyschedule.R
import com.example.polyschedule.data.network.models.schedule.ScheduleDto
import com.example.polyschedule.data.network.models.schedule.ScheduleResponse
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.schedule.ScheduleFragment

class ScheduleViewPagerAdapter(private val scheduleFragment: ScheduleFragment) : PagerAdapter() {
    var scheduleList = mutableListOf<ScheduleDto>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    val scheduleViewList: MutableList<ScheduleViewHolder?> = MutableList(8) { null }

    override fun getCount(): Int {
        return WeekDay.values().size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as View
    }


    override fun notifyDataSetChanged() {
        for (i in 0 until scheduleList.size) {
            val item = scheduleViewList[i]
            if (item != null){
                notifyItemChanged(item, i)
            }

        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }





    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = R.layout.schedule_item
        val item = ScheduleViewHolder(LayoutInflater.from(container.context).inflate(layout, container, false))
        scheduleViewList[position] = item
        container.addView(item.view)
        if (scheduleList.size - 1 > position){
            notifyItemChanged(item, position)
        }
        return item.view
    }

    private fun notifyItemChanged(item: ScheduleViewHolder, position: Int){
        item.recyclerView?.adapter =
            LessonAdapter(scheduleFragment).apply {
                lessonList = scheduleList[position].lessons
            }
    }

    class ScheduleViewHolder(val view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_schedule)
    }


}