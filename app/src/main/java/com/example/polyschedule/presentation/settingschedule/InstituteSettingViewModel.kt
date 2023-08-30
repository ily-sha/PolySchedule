package com.example.polyschedule.presentation.settingschedule

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polyschedule.data.repoimpl.ScheduleSettingRepositoryImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.usecase.schedulesetting.GetInstitutesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class InstituteSettingViewModel: ViewModel() {

    private val repository = ScheduleSettingRepositoryImpl()
    private val instituteUseCase = GetInstitutesUseCase(repository)
    val instituteLD = MutableLiveData<MutableList<Institute>>()

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    fun loadInstitute(){
        coroutineScope.launch {
            instituteLD.postValue(instituteUseCase.getInstitutes())
        }
    }


    val selectedCourse = MutableLiveData<Course>()
    val selectedInstitute = MutableLiveData<Institute>()

    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel()
    }


}