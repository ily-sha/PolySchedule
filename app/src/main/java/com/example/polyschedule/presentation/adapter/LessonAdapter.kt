package com.example.polyschedule.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Lesson

class LessonAdapter(private val lessonList: List<List<Lesson>>): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
//        val layout = when(viewType) {
//            COMPONENT_LESSON -> R.layout.
//        }
        return LessonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lesson_item, parent, false))
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        val item = lessonList[position][0]
        holder.lesson.text = item.subject
        holder.lessonType.text = item.lesson_type
        holder.teacherLL.visibility = if (item.teacher.isEmpty()) View.GONE else View.VISIBLE
        holder.teacher.text = item.teacher
        holder.time.text = "${item.time_start} - ${item.time_end}"
        val auditorium = if (item.auditories.contains(Regex("""\d+"""))) ", ${item.auditories}" else ""
        holder.place.text = "${item.building}$auditorium"
    }


    override fun getItemViewType(position: Int): Int {
        return if (lessonList[position].size == 1) SINGLE_LESSON else COMPONENT_LESSON
    }
    class LessonViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val time = view.findViewById<TextView>(R.id.tv_time)
        val place = view.findViewById<TextView>(R.id.tv_place)
        val teacher = view.findViewById<TextView>(R.id.tv_teacher)
        val lesson = view.findViewById<TextView>(R.id.tv_lesson_name)
        val lessonType = view.findViewById<TextView>(R.id.lesson_type)
        val teacherLL = view.findViewById<LinearLayout>(R.id.ll_teacher)
    }

    companion object {
        const val COMPONENT_LESSON = 10
        const val SINGLE_LESSON = 11
    }
}



