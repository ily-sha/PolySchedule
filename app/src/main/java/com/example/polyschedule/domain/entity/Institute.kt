package com.example.polyschedule.domain.entity

import org.json.JSONObject
import java.io.Serializable


data class Institute(val jsonObject: JSONObject, var selected: Boolean = false): Serializable{
    private val id = jsonObject.getInt("id")
    private val name = jsonObject.getString("name")
    private val abbr =jsonObject.getString("abbr")
    fun getId() = id
    fun getName() = name
    fun getAbbr() = abbr
}
