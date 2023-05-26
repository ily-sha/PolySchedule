package com.example.polyschedule.domain.usecase.direction

import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.repository.DirectionRepository

class AddDirectionUseCase(private val repository: DirectionRepository) {


    operator fun invoke(direction: Direction) = repository.addGroup(direction)

}