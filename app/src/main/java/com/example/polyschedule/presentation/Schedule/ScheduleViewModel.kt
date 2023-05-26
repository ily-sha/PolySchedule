package com.example.polyschedule.presentation.Schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.repoimpl.ScheduleRepositoryImpl
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.domain.usecase.scheduledb.GetLocalScheduleUseCase
import com.example.polyschedule.domain.usecase.schedulenetwork.GetNetworkScheduleUseCase
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
    private val repository = ScheduleRepositoryImpl(application)
    private val getNetworkScheduleUseCase = GetNetworkScheduleUseCase(repository)
    private val getLocalScheduleUseCase = GetLocalScheduleUseCase(repository)
//    private val getUniversityUseCase = GetUniversityUseCase(repository)


    val direction = MutableLiveData<Direction>()

    var scheduleLD = MutableLiveData<MutableList<Schedule>>()
    var currentWeekDay = MutableLiveData<WeekDay>()
//    private val removeUniversityUseCase = RemoveGroupUseCase(repository)

    private val getScheduleFromDbUseCase = GetLocalScheduleUseCase(repository)

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    fun getLocalSchedule() {
        viewModelScope.launch {
            var date = Calendar.getInstance().get(Calendar.DATE).toString()
//        if (date.length == 1) date = "0$date"
//        var month = (Calendar.getInstance().get(Calendar.MONTH) + 1).toString()
//        if (month.length == 1) month = "0$month"
//        val year = Calendar.getInstance().get(Calendar.YEAR).toString()
            getLocalScheduleUseCase.getSchedule()
        }
//
    }

    fun changeMainGroup(direction: Direction) {
        CacheUtils.instance?.setString(CacheUtils.MAIN_GROUP, direction.id.toString(), getApplication())
    }



//    fun getCurrentUniversity(id: Int): GroupEntity {
//        return getUniversityUseCase.getGroup(id)
//    }
//
//    fun getAllUniversities(): List<GroupEntity>{
//        return getUniversityUseCase.getAllUniversities()
//    }

    fun getCurrentSchedule(direction: Direction) {
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == SUNDAY) {
            getSchedule(direction.group.id, direction.institute.id, LocalDate.now().plusDays(1).toString())
        }
        coroutineScope.launch {
            scheduleLD.postValue(getNetworkScheduleUseCase.getCurrentSchedule(direction.group.id, direction.institute.id))
        }
    }


    fun getSchedule(groupId: Int, instituteId: Int, startDate: String){
        //date format 2022-04-22
        coroutineScope.launch {
            scheduleLD.postValue(getNetworkScheduleUseCase.getSchedule(groupId, instituteId, startDate))
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