package com.example.polyschedule.data.network.models.schedule

import android.os.Parcelable
import com.example.polyschedule.data.network.models.group.HomeworkResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScheduleResponse(val nextMonday: String, val previousMonday: String, val schedule: List<ScheduleDto>) :
    Parcelable

@Parcelize
data class ScheduleDto(val date: String, val lessons: Map<String, LessonDto>) : Parcelable


@Parcelize
data class LessonDto(
    val subject: String,
    val time: String,
    val lessonType: String,
    val auditory: String,
    val teachers: String?,
    val homework: List<HomeworkResponse>
) : Parcelable