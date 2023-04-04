package com.example.polyschedule.domain.entity

class Lesson(
    val subject: String,
    val time_start: String,
    val time_end: String,
    val typeObj: LessonType,
    val auditories: List<Auditorium>,
    val teachers: List<Teacher>?
) {

    fun getLessonType() =
        when (typeObj.name) {
            "Лек" -> "Лекция"
            "Лаб" -> "Лаба"
            else -> {
                typeObj.name
            }
        }


    fun getTeacher() = teachers.let {
        teachers?.get(0)?.full_name ?: ""
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
)

class LessonType(
    val name: String,
)