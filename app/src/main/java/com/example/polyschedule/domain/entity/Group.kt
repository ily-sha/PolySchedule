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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Group

        if (id != other.id) return false
        if (type != other.type) return false
        if (name != other.name) return false
        if (level != other.level) return false
        if (spec != other.spec) return false

        return true
    }


}