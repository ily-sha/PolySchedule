package com.example.polyschedule.domain.repository

import com.example.polyschedule.domain.entity.Direction

interface DirectionRepository {

    fun getDirection(id: Int): Direction

    fun getDirections(): List<Direction>

    fun addDirection(direction: Direction)

}