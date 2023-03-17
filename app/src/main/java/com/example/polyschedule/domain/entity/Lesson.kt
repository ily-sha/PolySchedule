package com.example.polyschedule.domain.entity

import org.json.JSONObject

open class Lesson(val id: Int, jsonObject: JSONObject) {
    val subject = jsonObject.getString("subject")
    val time_start = jsonObject.getString("time_start")
    val time_end = jsonObject.getString("time_end")
    var lesson_type = jsonObject.getJSONObject("typeObj").getString("name")
    val lesson_type_abbr = jsonObject.getJSONObject("typeObj").getString("abbr")
    val auditoriesJSONObject = jsonObject.getJSONArray("auditories")[0] as JSONObject
    val auditories = auditoriesJSONObject.getString("name")
    val building = auditoriesJSONObject.getJSONObject("building").getString("name")
    val teacherJSONObject = if (jsonObject.get("teachers")
            .equals(null)
    ) null else jsonObject.getJSONArray("teachers")[0] as JSONObject
    val teacher = teacherJSONObject?.getString("full_name") ?: ""


    init {
        if (lesson_type_abbr == "Лек") lesson_type = "Лекция"
        if (lesson_type_abbr == "Лаб") lesson_type = "Лаба"
    }


}