package com.example.polyschedule.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.usecase.GetInstitutesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class InstituteSettingViewModel(application: Application): AndroidViewModel(application) {

    private val repository = UniversityImpl(application)
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