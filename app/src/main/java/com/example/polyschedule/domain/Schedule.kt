package com.example.polyschedule.domain

import org.json.JSONObject

data class Schedule(val jsonObject: JSONObject){

    val weekday = jsonObject.getInt("weekday")
    val date = jsonObject.getString("date")
    val jsonArrayLesson = jsonObject.getJSONArray("lessons")
    private val _lessons = MutableList(jsonArrayLesson.length()) {
        Lesson(jsonArrayLesson.get(it) as JSONObject, it)
    }
    var lessons = mutableSetOf<List<Lesson>>()


    init {
        createComponentLesson()
    }

    private fun createComponentLesson(){
        for (i in 0 until  _lessons.size) {
            val pair = _lessons.filter { it.time_start == _lessons[i].time_start &&
                    it.time_end == _lessons[i].time_end }
            if (pair.size != 1) {
                lessons.add(pair.sortedBy { it.id})
            }
            else lessons.add(listOf(_lessons[i]))
        }



    }






}
