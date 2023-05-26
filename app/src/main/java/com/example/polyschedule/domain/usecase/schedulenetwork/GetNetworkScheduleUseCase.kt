package com.example.polyschedule.domain.usecase.schedulenetwork

import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.repository.ScheduleRepository

class GetNetworkScheduleUseCase(private val scheduleRepository: ScheduleRepository){

    suspend fun getCurrentSchedule(groupId: Int, instituteId: Int): MutableList<Schedule>{
        return scheduleRepository.getCurrentScheduleFromNetwork(groupId, instituteId)
    }

    suspend fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableList<Schedule>{
        return scheduleRepository.getScheduleFromNetwork(groupId, instituteId, startDate)
    }
}