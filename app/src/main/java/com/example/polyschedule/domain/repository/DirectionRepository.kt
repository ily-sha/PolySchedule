package com.example.polyschedule.domain.repository

import com.example.polyschedule.domain.entity.Direction

interface DirectionRepository {

    fun getGroup(id: Int): Direction

    fun getGroups(): List<Direction>

    fun addGroup(direction: Direction)

    fun removeGroup(id: Int)
}