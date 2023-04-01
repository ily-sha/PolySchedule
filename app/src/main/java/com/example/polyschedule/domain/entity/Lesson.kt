package com.example.polyschedule.domain.entity

import com.google.gson.JsonObject

class Lesson(
    val subject: String,
    val time_start: String,
    val time_end: String,
    val typeObj: LessonType,
    val auditories: List<Auditorium>,
    val teachers: List<Teacher>?
) {

    lateinit var lesson_type: String
    lateinit var teacher: String
    fun init(){
        teachers.let {
            teacher = teachers?.get(0)?.full_name ?: ""
        }
        lesson_type = when (typeObj.name) {
            "Лек" -> "Лекция"
            "Лаб" -> "Лаба"
            else -> {typeObj.name}
        }
    }
}

class Teacher(
    val full_name: String
)

class Auditorium(
    val name: String,
    val building: Building
)

class Building(
    val name: String,
    val abbr: String
)

class LessonType(
    val name: String,
    val abbr: String
)