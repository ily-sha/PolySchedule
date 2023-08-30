package com.example.polyschedule.domain.entity

import java.io.Serializable


data class Institute(
    val id: Int,
    val name: String,
    val abbr: String,
    var selected: Boolean = false
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Institute

        if (id != other.id) return false
        if (name != other.name) return false
        if (abbr != other.abbr) return false

        return true
    }

}
