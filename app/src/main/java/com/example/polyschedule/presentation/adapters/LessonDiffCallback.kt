package com.example.polyschedule.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.polyschedule.data.network.models.schedule.LessonDto
import com.example.polyschedule.domain.entity.Lesson

class LessonDiffCallback(
    private val oldList: Map<String, LessonDto>,
    private val newList: Map<String, LessonDto>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.entries.toList()[oldItemPosition].key == newList.entries.toList()[newItemPosition].key
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList.entries.toList()[oldItemPosition] == newList.entries.toList()[newItemPosition]
    }
}