package com.example.polyschedule.domain.repository

import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute

interface ScheduleSettingRepository {

    suspend fun getGroups(numberOfCourse: Int, instituteId: Int): MutableMap<String, MutableList<Group>>

    suspend fun getInstitute(): MutableList<Institute>

}