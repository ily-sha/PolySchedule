package com.example.polyschedule.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.GroupsImpl
import com.example.polyschedule.domain.GetGroupsUseCase
import com.example.polyschedule.domain.GetInstitutesUseCase
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.domain.Speciality

class MainViewModel: ViewModel() {
    companion object {
        var INSTITUTE_CHOOSEN: Institute? = null
        var COURSE_CHOOSEN: Int? = null
        const val COURSE1 = 1
        const val COURSE2 = 2
        const val COURSE3 = 3
        const val COURSE4 = 4
        const val COURSE5 = 5
        val GROUPE_CHOOSEN = false
    }

    private val repository = GroupsImpl
    private val getGroupsUseCase = GetGroupsUseCase(repository)
    private val getInstitutesUseCase = GetInstitutesUseCase(repository)

    val instituteList = MutableLiveData<List<Institute>>()

    fun updateRVGroupList(allGroups: MutableList<Speciality>) {
        allGroups.clear()
        getGroupsUseCase.getGroups(allGroups)
    }

    fun getInstitute(): List<Institute>{
        return getInstitutesUseCase.getInstitutes()
    }


}