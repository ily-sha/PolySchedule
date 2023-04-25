package com.example.polyschedule.domain.usecase

import androidx.lifecycle.LiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Institute

data class GetInstitutesUseCase(private val universityRepository: UniversityRepository){
    suspend fun getInstitutes(): MutableList<Institute> { return universityRepository.getInstitute()
    }
}
