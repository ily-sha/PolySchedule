package com.example.polyschedule.presentation.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.polyschedule.domain.entity.Lesson

class LessonDiffCallback(
    private val oldList: Map<String, List<Lesson>>,
    private val newList: Map<String, List<Lesson>>
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