package com.example.polyschedule.domain.usecase.scheduledb

import com.example.polyschedule.domain.repository.ScheduleRepository

class GetLocalScheduleUseCase(private val repository: ScheduleRepository) {

    suspend fun getSchedule() = repository.getScheduleFromDb()
}