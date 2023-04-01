package com.example.polyschedule.domain.entity

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

data class Schedule(
    val weekday: Int,
    val date: String,
    val lessons: JsonArray
) {
    lateinit var startWeek: LocalDate
    lateinit var lessonsMap: Map<String, List<Lesson>>
    fun init(startWeek: LocalDate) {
        this.startWeek = startWeek
        val _lessons = MutableList(lessons.size()) {
            Gson().fromJson(lessons[it].toString(), Lesson::class.java).apply {
                init()
            }
        }
        lessonsMap = _lessons.groupBy { it.time_start }
    }



    fun getPreviousMonday(): LocalDate = startWeek.minusDays(7)
    fun getNextMonday(): LocalDate = startWeek.plusDays(7)

    fun getDay() = date.split("-")[2]
    fun getMonth() = date.split("-")[1]
}
