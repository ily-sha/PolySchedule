package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.UniversityEntity

class GetUniversityUseCase(private val repository: UniversityRepository) {

    fun getUniversity(): UniversityEntity{
        return repository.getUniversity()
    }

    fun getAllUniversity(): List<UniversityEntity> {
        return repository.getAllUniversity()
    }
}