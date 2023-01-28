package com.example.polyschedule.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.GroupsImpl
import com.example.polyschedule.domain.*

class MainViewModel: ViewModel() {
    companion object {
        var coursesList = MutableList(5) { Course("Курс ${it + 1}", it, false) }
    }

    private val repository = GroupsImpl
    private val getGroupsUseCase = GetGroupsUseCase(repository)
    private val getInstitutesUseCase = GetInstitutesUseCase(repository)

    val course = MutableLiveData<Course?>()
    val institute = MutableLiveData<Institute>()

    init {
        course.value = null
    }



    val institutesList = listOf<Institute>()

    fun updateRVGroupList(allGroups: MutableList<Speciality>) {
        allGroups.clear()
        getGroupsUseCase.getGroups(allGroups)
    }

    fun getInstitute(): List<Institute>{
        return getInstitutesUseCase.getInstitutes()
    }


}