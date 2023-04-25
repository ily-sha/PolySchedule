package com.example.polyschedule.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.WeekDay

class ScheduleViewPagerAdapter(private val context: Context) : PagerAdapter() {
    var scheduleList = mutableListOf<Schedule>()
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
        val item = ScheduleViewHolder(
            LayoutInflater.from(container.context)
                .inflate(layout, container, false)
        )
        scheduleViewList[position] = item
        container.addView(item.view)
        if (scheduleList.size - 1 > position){
            notifyItemChanged(item, position)
        }
        return item.view
    }

    private fun notifyItemChanged(item: ScheduleViewHolder, position: Int){
        item.recyclerView?.adapter =
            LessonAdapter(context).apply {
                lessonList = scheduleList[position].lessonsMap
            }
    }

    class ScheduleViewHolder(val view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_schedule)
    }


}