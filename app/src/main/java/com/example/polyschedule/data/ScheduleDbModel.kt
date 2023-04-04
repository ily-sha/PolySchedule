package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.polyschedule.domain.entity.Lesson

@Entity
class ScheduleDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val weekday: Int,
    val date: String,
    val lessonsId: Int
)


@Entity
class LessonDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val subject: String,
    val time_start: String,
    val time_end: String,
    val typeObj: String,
    val numberOfAuditorium: String,
    val buildingName: String,
    val teacher: String
)




