package com.example.polyschedule.domain.entity

import org.json.JSONObject
import java.io.Serializable


data class Group(private val jsonObject: JSONObject, var selected: Boolean = false): Serializable {

    companion object {
        const val COMMON_TYPE = "common"
        const val DISTANCE_TYPE = "distance"
        const val EVENING_TYPE = "evening"

    }

    val id: Int
        get() = jsonObject.getInt("id")

    val type: String
        get() = jsonObject.getString("type")

    val groupId: String
        get() = jsonObject.getString("name")

    val spec:String
        get() = if (Regex("""\d+\.\d+\.\d+ [A-zА-я\-\. _]+""").matches(jsonObject.getString("spec"))) jsonObject.getString("spec") else ""

    val code: String?
        get() = if (Regex(""".+ .+""").matches(spec)) jsonObject.getString("spec").split(" ")[0] else null

    val level:Int
        get() = jsonObject.getInt("level")



}