package com.example.polyschedule.domain.entity

import java.io.Serializable


data class Institute(
    val id: Int,
    val name: String,
    val abbr: String,
    var selected: Boolean = false
) : Serializable
