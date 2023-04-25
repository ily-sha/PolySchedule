package com.example.polyschedule.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Schedule

class GetScheduleUseCase(private val universityRepository: UniversityRepository){

    suspend fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableList<Schedule>{
        return universityRepository.getCurrentWeekSchedule(groupId, instituteId)
    }

    suspend fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableList<Schedule>{
        return universityRepository.getSchedule(groupId, instituteId, startDate)
    }


}