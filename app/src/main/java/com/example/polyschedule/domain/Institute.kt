package com.example.polyschedule.domain

import org.json.JSONObject

data class Institute(val jsonObject: JSONObject){
    private val id = jsonObject.getInt("id")
    private val name = jsonObject.getString("name")
    private val abbr =jsonObject.getString("abbr")
    fun getId() = id
    fun getName() = name
    fun getAbbr() = abbr
}
