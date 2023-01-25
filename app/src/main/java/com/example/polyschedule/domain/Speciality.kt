package com.example.polyschedule.domain

import org.json.JSONObject

class Speciality(private val jsonObject: JSONObject) {


    val id: Int
        get() = jsonObject.getInt("id")

    val groupId: String
        get() = jsonObject.getString("name")

    val spec:String
        get() = if (Regex("""\d+\.\d+\.\d+ [A-zА-я\-\. _]+""").matches(jsonObject.getString("spec"))) jsonObject.getString("spec") else ""

    val code: String?
        get() = if (Regex(""".+ .+""").matches(spec)) jsonObject.getString("spec").split(" ")[0] else null

   val name: String?
        get() = if (spec.isNotEmpty()) jsonObject.getString("spec").split(Regex("""\d+\.\d+\.\d+ """))[1] else null

    val level:Int
        get() = jsonObject.getInt("level")



}