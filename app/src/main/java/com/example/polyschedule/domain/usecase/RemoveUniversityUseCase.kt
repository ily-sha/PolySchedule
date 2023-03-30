package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.UniversityEntity

class RemoveUniversityUseCase(private val repository: UniversityRepository) {

    operator fun invoke(id: Int) {
        repository.removeUniversity(id)
    }
}