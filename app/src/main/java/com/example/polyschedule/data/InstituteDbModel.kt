package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class InstituteDbModel(
    @PrimaryKey
    val id: Int,
    val name: String,
    val abbr: String
)
