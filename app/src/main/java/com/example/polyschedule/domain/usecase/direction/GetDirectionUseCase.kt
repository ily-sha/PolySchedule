package com.example.polyschedule.domain.usecase.direction

import com.example.polyschedule.domain.repository.DirectionRepository

class GetDirectionUseCase(private val repository: DirectionRepository) {

    fun getDirection(id: Int) = repository.getGroup(id)

    fun getDirections() = repository.getGroups()

}