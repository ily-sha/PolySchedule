package com.example.polyschedule.domain.entity

class UniversityEntity(
    val group: Group,
    val institute: Institute
): java.io.Serializable{
    companion object {
        const val AUTOGENERATE_ID = 0
    }
}