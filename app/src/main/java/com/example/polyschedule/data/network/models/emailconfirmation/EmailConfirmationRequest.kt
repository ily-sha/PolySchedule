package com.example.polyschedule.data.network.models.emailconfirmation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class EmailConfirmationRequest(val token: String, val code: Int) : Parcelable
