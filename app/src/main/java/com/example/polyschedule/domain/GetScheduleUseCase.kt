package com.example.polyschedule.domain

import androidx.lifecycle.MutableLiveData

class GetScheduleUseCase(private val universityRepository: UniversityRepository){

    fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>> {
        return universityRepository.getCurrentWeekSchedule(groupId, instituteId)
    }

    fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableList<Schedule> {
        return universityRepository.getSchedule(groupId, instituteId, startDate)
    }


}