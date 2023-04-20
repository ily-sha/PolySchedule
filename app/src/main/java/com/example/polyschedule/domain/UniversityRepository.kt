package com.example.polyschedule.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.ScheduleDbModel
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.UniversityEntity

interface UniversityRepository {
    fun getGroups(numberOfCourse: Int, instituteId: Int): MutableLiveData<MutableMap<String, MutableList<Group>>>
    fun getInstitute(): LiveData<MutableList<Institute>>

    fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>>

    fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableLiveData<MutableList<Schedule>>

    fun getUniversity(id: Int): UniversityEntity

    fun getAllUniversity(): List<UniversityEntity>
    fun addUniversity(universityEntity: UniversityEntity)

    fun removeUniversity(id: Int)

    fun getSchedule(date: String): Schedule

    fun addSchedule(schedule: Schedule)
}