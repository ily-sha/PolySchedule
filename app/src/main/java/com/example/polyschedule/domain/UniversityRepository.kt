package com.example.polyschedule.domain

interface UniversityRepository {
    fun getGroups(numberOfCourse: Int, instituteId: Int): MutableList<Group>
    fun getInstitute(): MutableList<Institute>
}