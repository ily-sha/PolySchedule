package com.example.polyschedule.presentation.Adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.polyschedule.domain.entity.Schedule

class ScheduleItemCallback: DiffUtil.ItemCallback<Schedule>() {


    override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem.lessons == newItem.lessons
    }

    override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
        return oldItem == newItem
    }
}