package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.UniversityEntity

class GetUniversityUseCase(private val repository: UniversityRepository) {

    fun getUniversity(id: Int): UniversityEntity {
        return repository.getUniversity(id)
    }

    fun getAllUniversities(): List<UniversityEntity> {
        return repository.getAllUniversity()
    }
}