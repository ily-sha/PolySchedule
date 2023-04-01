package com.example.polyschedule.domain.entity

data class UniversityEntity(
    val group: Group,
    val institute: Institute,
    val id: Int = DEFAULT_ID
): java.io.Serializable{
    companion object {
        const val AUTOGENERATE_ID = 0
        const val DEFAULT_ID = -1
    }
}