package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.usecase.GetGroupsUseCase

class GroupSettingViewModel(application: Application): AndroidViewModel(application) {

    private val getGroupsUseCase = GetGroupsUseCase(UniversityImpl(application))

    var groupLD = MutableLiveData<MutableMap<String, MutableList<Group>>>()
    var selectedGroup = MutableLiveData<Group>()

    fun getGroups(course: Course, institute: Institute) {
        groupLD = getGroupsUseCase.getGroups(course.numberOfCourse, institute.id)
    }

}
