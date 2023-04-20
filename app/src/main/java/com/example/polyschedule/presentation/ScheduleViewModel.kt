package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.domain.usecase.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(application: Application): AndroidViewModel(application) {
    private val SUNDAY = 0
    private val repository = UniversityImpl(application)
    private val getScheduleUseCase = GetScheduleUseCase(repository)
    private val getUniversityUseCase = GetUniversityUseCase(repository)



    var schedule = MutableLiveData<MutableList<Schedule>>()
    var currentWeekDay = MutableLiveData<WeekDay>()
    private val removeUniversityUseCase = RemoveUniversityUseCase(repository)

    private val getScheduleFromDbUseCase = GetScheduleFromDb(repository)





    fun getScheduleFromDb() {
        var date = Calendar.getInstance().get(Calendar.DATE).toString()
        if (date.length == 1) date = "0$date"
        var month = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
        if (month.length == 1) month = "0$month"
        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
        getScheduleFromDbUseCase("$year-$month-$date")
    }

    fun changeMainGroup(universityEntity: UniversityEntity) {
        CacheUtils.instance?.setString(CacheUtils.MAIN_GROUP, universityEntity.id.toString(), getApplication())
    }



    fun getCurrentUniversity(id: Int): UniversityEntity {
        return getUniversityUseCase.getUniversity(id)
    }

    fun getAllUniversities(): List<UniversityEntity>{
        return getUniversityUseCase.getAllUniversities()
    }

    fun getCurrentWeekSchedule(universityEntity: UniversityEntity) {
        schedule = getScheduleUseCase.getCurrentWeekSchedule(universityEntity.group.id, universityEntity.institute.id)
    }
    fun getScheduleOfParticularWeek(groupId: Int, instituteId: Int, startDate: String){
        schedule = getScheduleUseCase.getSchedule(groupId, instituteId, startDate)
    }

    fun formatDate(schedule: Schedule): String{
        val datePattern = SimpleDateFormat("d-MM", Locale("ru"))
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val day = DecimalFormat("#").format(schedule.getDay().toInt())
        return dateFormat.format(datePattern.parse("$day-${schedule.getMonth()}")!!)
    }

    fun getCurrentWeekDay(): Int {
        var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//            TODO("after day finished next day")
        if (currentWeekDay == SUNDAY) currentWeekDay = 1
        return currentWeekDay
    }



}