package com.example.polyschedule.domain

import androidx.lifecycle.MutableLiveData

interface UniversityRepository {
    fun getGroups(numberOfCourse: Int, instituteId: Int): MutableList<Group>
    fun getInstitute(): MutableList<Institute>

    fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>>

    fun getSchedule(groupId: Int, instituteId: Int, startDate: String): MutableList<Schedule>
}