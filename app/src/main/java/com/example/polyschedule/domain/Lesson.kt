package com.example.polyschedule.domain

import org.json.JSONArray
import org.json.JSONObject

data class Lesson(private val jsonObject: JSONObject, val id: Int) {


    val subject = jsonObject.getString("subject")
    val time_start = jsonObject.getString("time_start")
    val time_end = jsonObject.getString("time_end")
    val lesson_type = jsonObject.getJSONObject("typeObj").getString("name")
    val auditoriesJSONObject = jsonObject.getJSONArray("auditories")[0] as JSONObject
    val auditories = auditoriesJSONObject.getString("name")
    val building = auditoriesJSONObject.getJSONObject("building").getString("name")
    val teacherJSONObject = if (jsonObject.get("teachers").equals(null)) null else jsonObject.getJSONArray("teachers")[0] as JSONObject
    val teacher = teacherJSONObject?.getString("full_name") ?: ""



}