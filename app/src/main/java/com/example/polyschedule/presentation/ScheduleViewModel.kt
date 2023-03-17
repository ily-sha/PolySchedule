package com.example.polyschedule.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetScheduleUseCase
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.WeekDay
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel: ViewModel() {
    private val SUNDAY = 7
    private val repository = UniversityImpl
    private val getScheduleUseCase = GetScheduleUseCase(repository)


    var currentSchedule = MutableLiveData<MutableList<Schedule>>()

    var currentWeekDay = MutableLiveData<WeekDay>()


    fun getCurrentWeekSchedule(groupId: Int, instituteId: Int) {
        currentSchedule = getScheduleUseCase.getCurrentWeekSchedule(groupId, instituteId)
    }
    fun getScheduleOfParticularWeek(groupId: Int, instituteId: Int, startDate: String){
        currentSchedule = getScheduleUseCase.getSchedule(groupId, instituteId, startDate)
    }

    fun formateDate(schedule: Schedule): String{
        val datePattern = SimpleDateFormat("d-MM", Locale("ru"))
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val day = DecimalFormat("#").format(schedule.day.toInt())
        return dateFormat.format(datePattern.parse("$day-${schedule.month}")!!)
    }

    fun getCurrentWeekDay(): Int {
        var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//            TODO("after day finish next day")
        if (currentWeekDay == SUNDAY) currentWeekDay = WeekDay.MONDAY.position
        return currentWeekDay
    }

}