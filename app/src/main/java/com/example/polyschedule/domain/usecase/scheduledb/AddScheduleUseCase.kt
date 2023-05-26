package com.example.polyschedule.domain.usecase.scheduledb

import com.example.polyschedule.domain.repository.ScheduleRepository
import com.example.polyschedule.domain.entity.Schedule

class AddScheduleUseCase(private val repository: ScheduleRepository) {

    suspend operator fun invoke(schedule: Schedule) = repository.addScheduleToBd(schedule)
}