package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Schedule

class AddScheduleToDb(private val repository: UniversityRepository) {

    operator fun invoke(schedule: Schedule) = repository.addSchedule(schedule)
}