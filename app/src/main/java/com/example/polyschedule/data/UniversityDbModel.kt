package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class UniversityDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val instituteId: Int,
    val groupId: Int
)