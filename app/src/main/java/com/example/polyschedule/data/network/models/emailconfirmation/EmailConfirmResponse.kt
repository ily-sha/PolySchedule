package com.example.polyschedule.data.network.models.emailconfirmation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmailConfirmResponse(val token: String, val student: Student) : Parcelable

@Parcelize
data class Student(val name: String, val surname: String, val email: String, val groups: Map<Int, String>) :
    Parcelable

