package com.example.polyschedule.domain.usecase.schedulesetting

import com.example.polyschedule.domain.repository.ScheduleSettingRepository
import com.example.polyschedule.domain.entity.Institute

data class GetInstitutesUseCase(private val universityRepository: ScheduleSettingRepository){
    suspend fun getInstitutes(): MutableList<Institute> { return universityRepository.getInstitute()
    }
}
