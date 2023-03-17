package com.example.polyschedule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.presentation.ChooseAttributeViewModel.Companion.coursesList

class CourseAdapter: RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(val view: View): ViewHolder(view) {
        val tv = view.findViewById<TextView>(R.id.tv)

    }
    var onCourseItemClicked: ((Course) -> Unit)? = null

    private var lastSelected = FIRST_CLICK


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val layout = when(viewType){
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
        return coursesList.size
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.tv.text = coursesList[position].name
        holder.tv.setOnClickListener {
            if (lastSelected != FIRST_CLICK) {
                changeSelectedItemParams(lastSelected)
                notifyItemChanged(lastSelected)
            }
            changeSelectedItemParams(position)
            notifyItemChanged(position)
            lastSelected = position
            onCourseItemClicked?.invoke(coursesList[position])


        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (!coursesList[position].selected){
            if (position == 0) DISABLED_COURSE_FIRST_ITEM
            else DISABLED_COURSE_ITEM
        } else
            if (position == 0) ENABLED_COURSE_FIRST_ITEM
            else ENABLED_COURSE_ITEM
    }

    private fun changeSelectedItemParams(position: Int){
        coursesList.add(position, coursesList[position].copy(selected = !coursesList[position].selected))
        coursesList.remove(coursesList[position + 1])
    }

    companion object {
        const val ENABLED_COURSE_ITEM = 100
        const val ENABLED_COURSE_FIRST_ITEM = 1000
        const val DISABLED_COURSE_ITEM = 101
        const val DISABLED_COURSE_FIRST_ITEM = 1001

        const val FIRST_CLICK = -1
    }

}