package com.example.polyschedule.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class DirectionDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val instituteId: Int,
    val groupId: Int
)