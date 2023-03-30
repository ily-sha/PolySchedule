package com.example.polyschedule.domain.entity

class UniversityEntity(
    val group: Group,
    val institute: Institute
){
    companion object {
        const val DEFAULT_ID = 0
    }
}