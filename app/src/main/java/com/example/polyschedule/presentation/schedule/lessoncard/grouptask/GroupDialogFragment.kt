package com.example.polyschedule.presentation.schedule.lessoncard.grouptask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.polyschedule.R
import com.example.polyschedule.data.network.models.schedule.LessonDto
import org.w3c.dom.Text

class GroupDialogFragment(private val lesson: LessonDto, private val groups: Map<Int, String>) :
    DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.group_dialog, container)
        root.findViewById<TextView>(R.id.title).apply {
            text = "Выберите группу"
        }
        val linearLayout = root.findViewById<LinearLayout>(R.id.linearLayout)
        groups.forEach { group ->
            val cardView = layoutInflater.inflate(R.layout.light_green_button, linearLayout, false)
            val tv = cardView.findViewById<TextView>(R.id.inner_tv)
            tv.text = group.value
            cardView.setOnClickListener {
                startActivity(GroupTaskActivity.newInstance(requireContext(), lesson, group.key))
                dismiss()
            }
            linearLayout.addView(cardView)


        }
        return root
    }

}
