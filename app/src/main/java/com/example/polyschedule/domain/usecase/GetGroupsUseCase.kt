package com.example.polyschedule.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Group

data class GetGroupsUseCase(private val universityRepository: UniversityRepository) {

    suspend fun getGroups(numberOfCourse: Int, instituteId: Int): MutableMap<String, MutableList<Group>> {
        return universityRepository.getGroups(numberOfCourse, instituteId)
    }
}