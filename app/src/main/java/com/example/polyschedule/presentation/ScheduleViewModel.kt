package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetScheduleUseCase
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.domain.usecase.AddUniversityUseCase
import com.example.polyschedule.domain.usecase.GetUniversityUseCase
import com.example.polyschedule.domain.usecase.RemoveUniversityUseCase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ScheduleViewModel(application: Application): AndroidViewModel(application) {
    private val SUNDAY = 0
    private val repository = UniversityImpl(application)
    private val getScheduleUseCase = GetScheduleUseCase(repository)
    private val getUniversityUseCase = GetUniversityUseCase(repository)



    var currentSchedule = MutableLiveData<MutableList<Schedule>>()
    var currentWeekDay = MutableLiveData<WeekDay>()
    private val removeUniversityUseCase = RemoveUniversityUseCase(repository)
    private val addUniversityUseCase = AddUniversityUseCase(repository)

    fun removeUniversity(id: Int) = removeUniversityUseCase(id)


    fun changeMainGroup(universityEntity: UniversityEntity) {
        CacheUtils.instance?.setString(CacheUtils.MAIN_GROUP, universityEntity.id.toString(), getApplication())
    }

    fun addUniversity(universityEntity: UniversityEntity){
        addUniversityUseCase(universityEntity)
    }

    fun getCurrentUniversity(id: Int): UniversityEntity {
        return getUniversityUseCase.getUniversity(id)

    }

    fun getAllUniversities(): List<UniversityEntity>{
        return getUniversityUseCase.getAllUniversities()
    }

    fun getCurrentWeekSchedule(universityEntity: UniversityEntity) {
        currentSchedule = getScheduleUseCase.getCurrentWeekSchedule(universityEntity.group.id, universityEntity.institute.id)
    }
    fun getScheduleOfParticularWeek(groupId: Int, instituteId: Int, startDate: String){
        currentSchedule = getScheduleUseCase.getSchedule(groupId, instituteId, startDate)
    }

    fun formatDate(schedule: Schedule): String{
        val datePattern = SimpleDateFormat("d-MM", Locale("ru"))
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val day = DecimalFormat("#").format(schedule.day.toInt())
        return dateFormat.format(datePattern.parse("$day-${schedule.month}")!!)
    }

    fun getCurrentWeekDay(): Int {
        var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//            TODO("after day finished next day")
        if (currentWeekDay == SUNDAY) currentWeekDay = 1
        return currentWeekDay
    }

}