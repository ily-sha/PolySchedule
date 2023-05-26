package com.example.polyschedule.domain.usecase.schedulesetting

import com.example.polyschedule.domain.repository.ScheduleSettingRepository
import com.example.polyschedule.domain.entity.Group

data class GetGroupsUseCase(private val universityRepository: ScheduleSettingRepository) {

    suspend fun getGroups(numberOfCourse: Int, instituteId: Int): MutableMap<String, MutableList<Group>> {
        return universityRepository.getGroups(numberOfCourse, instituteId)
    }
}