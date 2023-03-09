package com.example.polyschedule.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Schedule

class GetScheduleUseCase(private val universityRepository: UniversityRepository){

    fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>> {
        return universityRepository.getCurrentWeekSchedule(groupId, instituteId)
    }

    fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableLiveData<MutableList<Schedule>> {
        return universityRepository.getSchedule(groupId, instituteId, startDate)
    }


}