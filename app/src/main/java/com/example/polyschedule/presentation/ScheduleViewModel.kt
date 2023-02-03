package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.GetScheduleUseCase
import com.example.polyschedule.domain.Schedule

class ScheduleViewModel: ViewModel() {

    private val repository = UniversityImpl
    private val getScheduleUseCase = GetScheduleUseCase(repository)


    var currentScheduleLD: MutableLiveData<MutableList<Schedule>>? = null

    fun getCurrentSchedule(groupId: Int, instituteId: Int) {

        currentScheduleLD = getScheduleUseCase.getCurrentWeekSchedule(groupId, instituteId)
    }
}