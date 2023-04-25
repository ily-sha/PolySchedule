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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


class ScheduleViewModel(application: Application): AndroidViewModel(application) {
    private val SUNDAY = 0
    private val repository = UniversityImpl(application)
    private val getScheduleUseCase = GetScheduleUseCase(repository)
    private val getUniversityUseCase = GetUniversityUseCase(repository)



    var scheduleLD = MutableLiveData<MutableList<Schedule>>()

    var universityEntityLD = MutableLiveData<UniversityEntity>()
    var currentWeekDay = MutableLiveData<WeekDay>()
    private val removeUniversityUseCase = RemoveUniversityUseCase(repository)

    private val getScheduleFromDbUseCase = GetScheduleFromDb(repository)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
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
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == SUNDAY){
            getScheduleOfParticularWeek(universityEntity.group.id, universityEntity.institute.id, LocalDate.now().plusDays(1).toString())
        }
        coroutineScope.launch {
            scheduleLD.postValue(getScheduleUseCase.getCurrentWeekSchedule(universityEntity.group.id, universityEntity.institute.id))
        }

    }


    fun getScheduleOfParticularWeek(groupId: Int, instituteId: Int, startDate: String){
        //date format 2022-04-22
        coroutineScope.launch {
            scheduleLD.postValue(getScheduleUseCase.getSchedule(groupId, instituteId, startDate))
        }

    }

    fun formatDate(schedule: Schedule): String{
        val datePattern = SimpleDateFormat("d-MM", Locale("ru"))
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val day = DecimalFormat("#").format(schedule.getDay().toInt())
        return dateFormat.format(datePattern.parse("$day-${schedule.getMonth()}")!!)
    }

    fun getCurrentWeekDay(): Int {
        //            TODO("after day finished next day")
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == SUNDAY){
            return 1
        }
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    }



}