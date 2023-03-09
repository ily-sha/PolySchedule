package com.example.polyschedule.domain.entity

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDate

data class Schedule(val jsonObject: JSONObject, val startWeek: LocalDate){
    @RequiresApi(Build.VERSION_CODES.O)
    val previousFirstMonday = startWeek.minusDays(7)
    @RequiresApi(Build.VERSION_CODES.O)
    val nextFirstMonday = startWeek.plusDays(7)
    val weekday = jsonObject.getInt("weekday")
    val date = jsonObject.getString("date")
    val jsonArrayLesson = jsonObject.getJSONArray("lessons")
    val lessons = MutableList(jsonArrayLesson.length()) {
        Lesson(jsonArrayLesson.get(it) as JSONObject)
    }







}
