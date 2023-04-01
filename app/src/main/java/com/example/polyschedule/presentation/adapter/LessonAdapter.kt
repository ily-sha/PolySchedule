package com.example.polyschedule.presentation.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.entity.Lesson
import com.example.polyschedule.presentation.LessonDiffCallback


class LessonAdapter(val context: Context) : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    var lessonList = mutableMapOf<String, List<Lesson>>()
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
        val layout = if (viewType == MULTI_LESSON) {
            R.layout.multi_lesson
        } else R.layout.lesson_item
        return LessonViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (lessonList.entries.toList()[position].value.size == 1) ORDINARY_LESSON else MULTI_LESSON
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.root.animation = AnimationUtils.loadAnimation(holder.view.context, R.anim.alpha)
        val item = lessonList.entries.toList()[position]
        if (item.value.size != 1) {
            holder.radioGroup.setOnCheckedChangeListener(object :
                RadioGroup.OnCheckedChangeListener {
                override fun onCheckedChanged(p0: RadioGroup?, p1: Int) {
                    println("$p1, ${holder.radioGroup.id}")
                }

            })
            for (i in 0..4) {
                val radioButton = RadioButton(context)
                radioButton.setTextColor(context.resources.getColor(R.color.white))
                radioButton.buttonTintList = radioButtonColor
                radioButton.textSize = 16F
                radioButton.typeface = context.resources.getFont(R.font.inter_medium)
                if (i == 0) {
                    radioButton.isChecked = true
                }
                holder.radioGroup.addView(radioButton)

            }
        }
        fillValue(holder, item.value[0])
    }

    companion object {
        private const val MULTI_LESSON = 10
        private const val ORDINARY_LESSON = 11

    }

    fun fillValue(holder: LessonViewHolder, item: Lesson) {
        holder.lesson.text = item.subject
        holder.lessonType.text = item.lesson_type
        holder.teacherLL.visibility = if (item.teacher.isEmpty()) View.GONE else View.VISIBLE
        holder.teacher.text = item.teacher
        val content = SpannableString("${item.time_start} - ${item.time_end}")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        holder.time.text = content
        val auditorium =
            if (item.auditories[0].name.contains(Regex("""\d+"""))) ", ${item.auditories[0].name}" else ""
        holder.place.text = "${item.auditories[0].building.name}$auditorium"
    }


    class LessonViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val time = view.findViewById<TextView>(R.id.tv_time)
        val place = view.findViewById<TextView>(R.id.tv_place)
        val teacher = view.findViewById<TextView>(R.id.tv_teacher)
        val lesson = view.findViewById<TextView>(R.id.tv_lesson_name)
        val lessonType = view.findViewById<TextView>(R.id.lesson_type)
        val teacherLL = view.findViewById<LinearLayout>(R.id.ll_teacher)
        val placeLL = view.findViewById<LinearLayout>(R.id.ll_place)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)
        val root = view.findViewById<ConstraintLayout>(R.id.root_lesson_container)




    }


}




