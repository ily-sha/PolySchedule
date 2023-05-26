package com.example.polyschedule.presentation.SettingSchedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.repoimpl.DirectionRepositoryImpl
import com.example.polyschedule.data.repoimpl.ScheduleSettingRepositoryImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.usecase.direction.AddDirectionUseCase
import com.example.polyschedule.domain.usecase.direction.GetDirectionUseCase
import com.example.polyschedule.domain.usecase.schedulesetting.GetGroupsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class GroupSettingViewModel(application: Application): AndroidViewModel(application) {
    private val scheduleSettingRepository = ScheduleSettingRepositoryImpl()
    private val getGroupsUseCase = GetGroupsUseCase(scheduleSettingRepository)

    private val directionRepository = DirectionRepositoryImpl(application)
    private val addDirectionUseCase = AddDirectionUseCase(directionRepository)

    var groupLD = MutableLiveData<MutableMap<String, MutableList<Group>>>()
    var selectedGroup = MutableLiveData<Group>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getGroups(course: Course, institute: Institute) {
        coroutineScope.launch {
            groupLD.postValue(getGroupsUseCase.getGroups(course.numberOfCourse, institute.id))
        }

    }

    fun setMainDirection(direction: Direction){
        addDirectionUseCase(direction)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}
