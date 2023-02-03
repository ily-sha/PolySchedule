package com.example.polyschedule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Lesson
import com.example.polyschedule.domain.Schedule

class LessonAdapter(private val lessonList: List<Lesson>): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        return LessonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false))
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val item = lessonList[position]
        holder.lesson.text = item.subject
        holder.lessonType.text = item.lesson_type
        holder.teacher.text = item.teacher ?: "OKOKO"
        holder.time.text = "${item.time_start} - ${item.time_end}"

    }
    class LessonViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val time = view.findViewById<TextView>(R.id.tv_time)
        val place = view.findViewById<TextView>(R.id.tv_place)
        val teacher = view.findViewById<TextView>(R.id.tv_teacher)
        val lesson = view.findViewById<TextView>(R.id.tv_lesson_name)
        val lessonType = view.findViewById<TextView>(R.id.lesson_type)

    }
}



