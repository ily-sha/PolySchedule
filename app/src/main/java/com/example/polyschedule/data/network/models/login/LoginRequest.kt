package com.example.polyschedule.data.network.models.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LoginRequest(val email: String) : Parcelable