package com.example.polyschedule.presentation.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.data.network.models.group.HomeworkResponse
import com.example.polyschedule.data.network.models.schedule.LessonDto
import com.example.polyschedule.presentation.schedule.ScheduleFragment
import com.google.android.material.card.MaterialCardView


class LessonAdapter(val scheduleFragment: ScheduleFragment) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {

    interface OnLessonClicked {
        fun onLessonClick(lesson: LessonDto)
    }
    var lessonList = mapOf<String, LessonDto>()
        set(value) {
            val callBack = LessonDiffCallback(lessonList, value)
            val diff = DiffUtil.calculateDiff(callBack)
            diff.dispatchUpdatesTo(this)
            field = value
        }

    private val radioButtonColor = ColorStateList(
        arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        ), intArrayOf(
            Color.BLACK,  // disabled
            Color.WHITE // enabled
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
//        val layout = if (viewType == MULTI_LESSON) {
//            R.layout.multi_lesson
//        } else R.layout.lesson_item
        val layout = R.layout.lesson_item
        return LessonViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

//    override fun getItemViewType(position: Int): Int {
//        return if (lessonList.entries.toList()[position].value.size == 1) ORDINARY_LESSON else MULTI_LESSON
//    }
    override fun getItemViewType(position: Int): Int {
        return ORDINARY_LESSON
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
//        holder.root.animation = AnimationUtils.loadAnimation(holder.view.context, R.anim.alpha)
        val item = lessonList.entries.toList()[position]
//        if (item.value.size != 1) {
//            holder.radioGroup.setOnCheckedChangeListener(object :
//                RadioGroup.OnCheckedChangeListener {
//                override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
//                    println("$p1, ${holder.radioGroup.id}")
//                }
//
//            })
//            for (i in 0..4) {
//                val radioButton = RadioButton(context)
//                radioButton.setTextColor(context.resources.getColor(R.color.white))
//                radioButton.buttonTintList = radioButtonColor
//                radioButton.textSize = 16F
//                radioButton.typeface = context.resources.getFont(R.font.inter_medium)
//                if (i == 0) {
//                    radioButton.isChecked = true
//                }
//                holder.radioGroup.addView(radioButton)
//
//            }
//        }
        fillValue(holder, item.value)
    }

    companion object {
        private const val MULTI_LESSON = 10
        private const val ORDINARY_LESSON = 11

    }

    private fun fillValue(holder: LessonViewHolder, item: LessonDto) {
        holder.view.setOnClickListener {
            (scheduleFragment as OnLessonClicked).onLessonClick(item)
        }
        holder.lesson.text = item.subject
        holder.lessonType.text = item.lessonType
        holder.teacherContainer.visibility = if (item.teachers == null){
            View.GONE
        } else {
            holder.teacher.text = item.teachers
            View.VISIBLE
        }
        holder.time.text = item.time

        holder.place.text = item.auditory
    }


    class LessonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val time = view.findViewById<TextView>(R.id.tv_time)
        val place = view.findViewById<TextView>(R.id.tv_location)
        val teacher = view.findViewById<TextView>(R.id.tv_teacher)
        val lesson = view.findViewById<TextView>(R.id.tv_lesson_name)
        val lessonType = view.findViewById<TextView>(R.id.lesson_type)
        val teacherContainer = view.findViewById<MaterialCardView>(R.id.teacher_container)
        val locationContainer = view.findViewById<MaterialCardView>(R.id.location_container)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)




    }


}




