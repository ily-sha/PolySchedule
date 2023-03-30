package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("InstituteDb")
data class InstituteDbModel(
    @PrimaryKey
    val id: Int,
    val abbr: String,
    val name: String
)
