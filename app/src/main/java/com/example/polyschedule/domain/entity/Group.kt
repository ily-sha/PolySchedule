package com.example.polyschedule.domain.entity

import java.io.Serializable


data class Group(
    val id: Int,
    val type: String,
    val name: String,
    val level: Int,
    val spec: String,
    var selected: Boolean = false
) : Serializable {

    companion object {
        const val COMMON_TYPE = "common"
        const val DISTANCE_TYPE = "distance"
        const val EVENING_TYPE = "evening"

    }


}