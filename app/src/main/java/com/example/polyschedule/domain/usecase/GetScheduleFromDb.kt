package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository

class GetScheduleFromDb(private val repository: UniversityRepository) {

    operator fun invoke(date: String) = repository.getSchedule(date)
}