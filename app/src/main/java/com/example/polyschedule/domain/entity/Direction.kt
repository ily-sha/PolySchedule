package com.example.polyschedule.domain.entity

data class Direction(
    val group: Group,
    val institute: Institute,
    val id: Int = DEFAULT_ID
): java.io.Serializable {


    companion object {
        const val AUTOGENERATE_ID = 0
        const val DEFAULT_ID = -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Direction

        if (group != other.group) return false
        if (institute != other.institute) return false

        return true
    }

}