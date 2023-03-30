package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute

@Entity
data class UniversityDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val isMainGroup: Boolean,
    val groupDbModelId: Int,
    val instituteDbModelId: Int
) {
}