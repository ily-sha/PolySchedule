package com.example.polyschedule.presentation.schedule.lessoncard.grouptask


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.polyschedule.R
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.models.schedule.LessonDto


class HomeworkDialogFragment(private val lessonDto: LessonDto) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.group_dialog, container)
        root.findViewById<TextView>(R.id.title).apply {
            text = "Выберите задание"
        }
        var homeworkFromSingleGroup = true
        if (lessonDto.homework.groupBy { it.groupId }.size > 1){
            homeworkFromSingleGroup = false
        }
        val linearLayout = root.findViewById<LinearLayout>(R.id.linearLayout)
        lessonDto.homework.forEach { homework ->
            val cardView = layoutInflater.inflate(R.layout.light_green_button, linearLayout, false)
            val tv = cardView.findViewById<TextView>(R.id.inner_tv)
            var groupDescription = ""
            if (!homeworkFromSingleGroup){
                CachedStudentUtils.getStudentInfo(requireContext().applicationContext)?.apply {
                    studentGroups[homework.groupId]?.let {
                        groupDescription = "Группа: ${it}\n"
                    }

                }
            }
            var homeworkDescription = ""
            if (homework.description.isNotBlank()) {
                homeworkDescription = "Описание: ${homework.description}"
            }
            tv.text = groupDescription + homeworkDescription
            cardView.setOnClickListener {
                startActivity(GroupTaskActivity.newInstance(requireContext(), lessonDto, homework.groupId))
                dismiss()
            }
            linearLayout.addView(cardView)


        }
        return root


    }

}