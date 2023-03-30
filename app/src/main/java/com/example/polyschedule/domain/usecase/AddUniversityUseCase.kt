package com.example.polyschedule.domain.usecase

import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.UniversityEntity

class AddUniversityUseCase(private val repository: UniversityRepository) {


     operator fun invoke(universityEntity: UniversityEntity) {
         repository.addUniversity(universityEntity)
     }
}