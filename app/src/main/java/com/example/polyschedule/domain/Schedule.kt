package com.example.polyschedule.domain

import org.json.JSONObject

data class Schedule(val jsonObject: JSONObject){
    val weekday = jsonObject.getInt("weekday")
    val date = jsonObject.getString("date")
    val jsonArrayLesson = jsonObject.getJSONArray("lessons")
    val lessons = List(jsonArrayLesson.length()) {
        Lesson(jsonArrayLesson.get(it) as JSONObject)
    }



}
