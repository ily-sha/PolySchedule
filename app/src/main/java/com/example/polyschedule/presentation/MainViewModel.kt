package com.example.polyschedule.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.*

class MainViewModel: ViewModel() {

    companion object {
        var coursesList = MutableList(5) { Course("Курс ${it + 1}", it, false)}
    }

    private val repository = UniversityImpl
    private val getGroupsUseCase = GetGroupsUseCase(repository)
    private val getInstitutesUseCase = GetInstitutesUseCase(repository)

    val courseLD = MutableLiveData<Course>()
    val groupLD = MutableLiveData<Group>()
    val instituteLD = MutableLiveData<Institute>()

    fun getGroups(numberOfCourse: Int, instituteId: Int): MutableList<Group>{
        return getGroupsUseCase.getGroups(numberOfCourse, instituteId)
    }

    fun getInstitutes(): MutableList<Institute>{
        return getInstitutesUseCase.getInstitutes()
    }


}