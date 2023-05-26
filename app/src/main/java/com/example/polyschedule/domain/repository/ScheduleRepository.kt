package com.example.polyschedule.domain.repository

import com.example.polyschedule.domain.entity.Schedule

interface ScheduleRepository {

    suspend fun getCurrentScheduleFromNetwork(groupId: Int, instituteId: Int): MutableList<Schedule>

    suspend fun getScheduleFromNetwork(groupId: Int, instituteId: Int, startDate: String): MutableList<Schedule>


    suspend fun getScheduleFromDb(): Schedule

    suspend fun addScheduleToBd(schedule: Schedule)
}