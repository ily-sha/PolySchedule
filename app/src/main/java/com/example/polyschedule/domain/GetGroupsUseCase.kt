package com.example.polyschedule.domain

data class GetGroupsUseCase(private val universityRepository: UniversityRepository) {

    fun getGroups(numberOfCourse: Int, instituteId: Int): MutableList<Group>{
        return universityRepository.getGroups(numberOfCourse, instituteId)
    }
}