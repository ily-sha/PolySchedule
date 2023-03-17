package com.example.polyschedule.domain.entity

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDate

data class Schedule(val jsonObject: JSONObject, val startWeek: LocalDate){
    val previousMonday = startWeek.minusDays(7)
    val nextMonday = startWeek.plusDays(7)
    val weekday = jsonObject.getInt("weekday")
    val date = jsonObject.getString("date")
    var year = ""
    var month = ""
    var day = ""
    val jsonArrayLesson = jsonObject.getJSONArray("lessons")
    private val _lessons = MutableList(jsonArrayLesson.length()) {
        Lesson(it, jsonArrayLesson.get(it) as JSONObject)
    }
    var lessons = mapOf<String, List<Lesson>>()
    init {
        lessons = _lessons.groupBy { it.time_start }
        val (_year, _month, _day) = date.split("-")
        year = _year
        month = _month
        day = _day
    }








}
