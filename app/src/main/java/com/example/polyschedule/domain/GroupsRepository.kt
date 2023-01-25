package com.example.polyschedule.domain

interface GroupsRepository {
    fun getGroups(groups: MutableList<Speciality>)
    fun getInstitute(): List<Institute>
}