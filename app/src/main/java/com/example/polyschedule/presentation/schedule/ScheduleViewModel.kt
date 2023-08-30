package com.example.polyschedule.presentation.schedule

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.schedule.ScheduleResponse
import com.example.polyschedule.data.repoimpl.DirectionRepositoryImpl
import com.example.polyschedule.data.repoimpl.ScheduleRepositoryImpl
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.domain.usecase.direction.AddDirectionUseCase
import com.example.polyschedule.domain.usecase.direction.GetDirectionUseCase
import com.example.polyschedule.domain.usecase.scheduledb.GetLocalScheduleUseCase
import com.example.polyschedule.domain.usecase.schedulenetwork.GetNetworkScheduleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.Socket
import java.net.SocketTimeoutException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale


class ScheduleViewModel(private val application: Application) : AndroidViewModel(application) {

    private val SUNDAY = 0
    private val scheduleRepository = ScheduleRepositoryImpl(application)
    private val getNetworkScheduleUseCase = GetNetworkScheduleUseCase(scheduleRepository)
    private val getLocalScheduleUseCase = GetLocalScheduleUseCase(scheduleRepository)


    private val directionRepository = DirectionRepositoryImpl(application)
    private val getDirectionUseCase = GetDirectionUseCase(directionRepository)
    private val addDirectionUseCase = AddDirectionUseCase(directionRepository)


    val direction = MutableLiveData<Direction>()

    var scheduleLD = MutableLiveData<ScheduleResponse>()
    var currentWeekDay = MutableLiveData<WeekDay>()


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

    fun changeMainDirection(direction: Direction) {
        CacheUtils.setString(
            CacheUtils.DIRECTION,
            direction.id.toString(),
            getApplication()
        )
    }


    fun getDirections() = getDirectionUseCase.getDirections()
    fun setMainDirection(selectedDirection: Direction) {
        CacheUtils.setString(
            CacheUtils.DIRECTION,
            selectedDirection.id.toString(),
            application
        )
        direction.value = selectedDirection
        getSchedule()
    }

//
//    fun getAllUniversities(): List<GroupEntity>{
//        return getUniversityUseCase.getAllUniversities()
//    }

    fun getSchedule(startDate: String? = null) {
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == SUNDAY) {
            getSchedule(LocalDate.now().plusDays(1).toString())
        }
        val token = CachedStudentUtils.getToken(application)
        coroutineScope.launch {
            direction.value?.let {
                val group = it.group.id.toString()
                val institute = it.institute.id.toString()
                try {
                    val response = Api.retrofitService.getSchedule(
                        token = token,
                        groupId = group,
                        instituteId = institute,
                        startDate = startDate
                    )
                    handleScheduleResponse(response)
                } catch (e: SocketTimeoutException){
                    withContext(Dispatchers.Main){

                    }
                }




            }
        }
    }

    private fun handleScheduleResponse(response: Response<ScheduleResponse>){
        when (response.code()) {
            200 -> {
                val body = response.body()
                body?.let {
                    scheduleLD.postValue(it)
                }
            }
            else -> {

            }
        }

    }


    fun getDateFromMillisecond(milliSeconds: Long): String {
        val simple: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val result: Date = Date(milliSeconds)
        return simple.format(result)
    }

    fun formatDate(schedule: Schedule): String {
        val datePattern = SimpleDateFormat("d-MM", Locale("ru"))
        val dateFormat = SimpleDateFormat("d MMMM", Locale("ru"))
        val day = DecimalFormat("#").format(schedule.getDay().toInt())
        return dateFormat.format(datePattern.parse("$day-${schedule.getMonth()}")!!)
    }

    fun getCurrentWeekDay(): Int {
        //            TODO("after day finished next day")
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 == SUNDAY) {
            return 1
        }
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
    }

    fun clearMainDirection() {
        CacheUtils.removeString(CacheUtils.DIRECTION, getApplication())
    }


}