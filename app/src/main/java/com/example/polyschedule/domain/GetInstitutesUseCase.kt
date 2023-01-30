package com.example.polyschedule.domain

data class GetInstitutesUseCase(private val universityRepository: UniversityRepository){
    fun getInstitutes():MutableList<Institute>{
        return universityRepository.getInstitute()
    }
}
