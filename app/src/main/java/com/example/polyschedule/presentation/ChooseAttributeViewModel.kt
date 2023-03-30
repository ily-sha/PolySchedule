package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.usecase.AddUniversityUseCase
import com.example.polyschedule.domain.usecase.GetGroupsUseCase
import com.example.polyschedule.domain.usecase.GetInstitutesUseCase
import com.example.polyschedule.domain.usecase.GetUniversityUseCase

class ChooseAttributeViewModel(application: Application): AndroidViewModel(application) {



    private val repository = UniversityImpl(application)
    private val getGroupsUseCase = GetGroupsUseCase(repository)
    var groupLDfromOut = MutableLiveData<MutableList<Group>>()

    val instituteLDfromOut = GetInstitutesUseCase(repository).getInstitutes()



    fun addUniversityBd(universityEntity: UniversityEntity){
        AddUniversityUseCase(repository).invoke(universityEntity)
    }


    val courseLD = MutableLiveData<Course>()
    val groupLD = MutableLiveData<Group>()
    val instituteLD = MutableLiveData<Institute>()

    fun getGroups(numberOfCourse: Int, instituteId: Int){
        groupLDfromOut = getGroupsUseCase.getGroups(numberOfCourse, instituteId)
    }
}