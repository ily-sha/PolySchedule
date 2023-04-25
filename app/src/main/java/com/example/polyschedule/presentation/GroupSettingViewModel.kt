package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.usecase.GetGroupsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class GroupSettingViewModel(application: Application): AndroidViewModel(application) {
    private val repository = UniversityImpl(application)
    private val getGroupsUseCase = GetGroupsUseCase(repository)

    var groupLD = MutableLiveData<MutableMap<String, MutableList<Group>>>()
    var selectedGroup = MutableLiveData<Group>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun getGroups(course: Course, institute: Institute) {
        coroutineScope.launch {
            groupLD.postValue(getGroupsUseCase.getGroups(course.numberOfCourse, institute.id))
        }

    }

    fun setMainSchedule(universityEntity: UniversityEntity){
        repository.addUniversity(universityEntity)
    }

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }

}
