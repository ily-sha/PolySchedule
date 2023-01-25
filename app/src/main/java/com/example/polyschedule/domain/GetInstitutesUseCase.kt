package com.example.polyschedule.domain

data class GetInstitutesUseCase(private val groupsRepository: GroupsRepository){
    fun getInstitutes():List<Institute>{
        return groupsRepository.getInstitute()
    }
}
