package com.example.polyschedule.domain.usecase.direction

import com.example.polyschedule.domain.repository.DirectionRepository

class RemoveDirectionUseCase(private val repository: DirectionRepository) {

    operator fun invoke(id: Int) {
        repository.removeGroup(id)
    }
}