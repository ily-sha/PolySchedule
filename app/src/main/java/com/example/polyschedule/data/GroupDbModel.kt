package com.example.polyschedule.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDbModel(
    @PrimaryKey
    val id: Int,
    val level: Int,
    val name: String,
    val spec: String,
    val type: String
) {
}