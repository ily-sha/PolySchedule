package com.example.polyschedule.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Lesson(
    val subject: String,
    val time_start: String,
    val time_end: String,
    val typeObj: LessonType,
    val auditories: List<Auditorium>,
    val teachers: List<Teacher>?
) : Parcelable {

    fun getLessonType() =
        when (typeObj.name) {
            "Лекции" -> "Лекция"
            "Лабораторные" -> "Лаба"
            else -> typeObj.name

        }


    fun getTeacher() = teachers.let { it?.get(0)?.full_name ?: "" }

}

@Parcelize
class Teacher(
    val full_name: String
) : Parcelable

@Parcelize
class Auditorium(
    val name: String,
    val building: Building
) : Parcelable

@Parcelize
class Building(
    val name: String,
) : Parcelable

@Parcelize
class LessonType(
    val name: String,
) : Parcelable