package com.example.polyschedule.presentation

import android.content.Context
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.usecase.GetGroupsUseCase
import com.example.polyschedule.domain.usecase.GetInstitutesUseCase

class MainViewModel(val lifecycleEventOwner: LifecycleOwner): ViewModel() {

    companion object {
        var coursesList = MutableList(5) { Course("Курс ${it + 1}", it, false) }
    }

    private val repository = UniversityImpl
    private val getGroupsUseCase = GetGroupsUseCase(repository)
    var groupLDfromOut = MutableLiveData<MutableList<Group>>()

    val instituteLDfromOut = GetInstitutesUseCase(repository).getInstitutes()

    val courseLD = MutableLiveData<Course>()
    val groupLD = MutableLiveData<Group>()
    val instituteLD = MutableLiveData<Institute>()

    fun getGroups(numberOfCourse: Int, instituteId: Int){
        groupLDfromOut = getGroupsUseCase.getGroups(numberOfCourse, instituteId)
    }
}