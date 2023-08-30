package com.example.polyschedule.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Course

class CourseAdapter : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(val view: View) : ViewHolder(view){
        val tv = view.findViewById<TextView>(R.id.schedule_setting_tv)
    }

    var onCourseItemClicked: ((Course) -> Unit)? = null

    private var lastSelected = FIRST_CLICK

    fun clear() {
        extrudeLastView(lastSelected)
        lastSelected = FIRST_CLICK
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val layout = when (viewType) {
            DISABLED_COURSE_ITEM -> R.layout.disabled_course_item
            DISABLED_COURSE_FIRST_ITEM -> R.layout.disabled_course_first_item
            ENABLED_COURSE_FIRST_ITEM -> R.layout.enabled_course_first_item
            ENABLED_COURSE_ITEM -> R.layout.enabled_course_item
            else -> throw java.lang.RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Course.values().size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {

        val course = getCourseByPosition(position + 1)
        holder.tv.text = course.nameOfCourse
        holder.tv.setOnClickListener {
            if (lastSelected != FIRST_CLICK) {
                extrudeLastView(lastSelected)
            }
            extrudeLastView(position)
            lastSelected = position
            onCourseItemClicked?.invoke(course)
        }


    }

    override fun getItemViewType(position: Int): Int {
        val course = getCourseByPosition(position + 1)
        return if (course.enable) {
            if (position == 0) ENABLED_COURSE_FIRST_ITEM
            else ENABLED_COURSE_ITEM
        } else
            if (position == 0) DISABLED_COURSE_FIRST_ITEM
            else DISABLED_COURSE_ITEM
    }


    private fun extrudeLastView(position: Int) {
        if (position >=0){
            val course = getCourseByPosition(position + 1)
            course.enable = !course.enable
            notifyItemChanged(position)
        }

    }


    companion object {
        const val ENABLED_COURSE_ITEM = 100
        const val ENABLED_COURSE_FIRST_ITEM = 1000
        const val DISABLED_COURSE_ITEM = 101
        const val DISABLED_COURSE_FIRST_ITEM = 1001

        const val FIRST_CLICK = -1
    }
    private fun getCourseByPosition(position: Int): Course{
        return when (position) {
            Course.FIRST.numberOfCourse -> Course.FIRST
            Course.SECOND.numberOfCourse -> Course.SECOND
            Course.THIRD.numberOfCourse -> Course.THIRD
            Course.FOURTH.numberOfCourse -> Course.FOURTH
            Course.FIFTH.numberOfCourse -> Course.FIFTH
            else -> throw RuntimeException("Course with numberOfCourse $position is not exist")

        }
    }

}