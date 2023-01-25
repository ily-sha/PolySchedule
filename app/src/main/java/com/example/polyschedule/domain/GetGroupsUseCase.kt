package com.example.polyschedule.domain

data class GetGroupsUseCase(private val groupsRepository: GroupsRepository) {

    fun getGroups(allGroups: MutableList<Speciality>){
        groupsRepository.getGroups(allGroups)
    }
}