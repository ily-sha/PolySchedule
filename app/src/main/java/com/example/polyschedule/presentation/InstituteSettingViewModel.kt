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

class InstituteSettingViewModel(application: Application): AndroidViewModel(application) {



    private val repository = UniversityImpl(application)


    val instituteLDfromOut = GetInstitutesUseCase(repository).getInstitutes()






    val selectedCourse = MutableLiveData<Course>()
    val selectedInstitute = MutableLiveData<Institute>()


}