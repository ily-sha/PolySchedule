package com.example.polyschedule.data.database.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class GroupDbModel(
    @PrimaryKey
    val id: Int,
    val type: String,
    val name: String,
    val level: Int,
    val spec: String
)