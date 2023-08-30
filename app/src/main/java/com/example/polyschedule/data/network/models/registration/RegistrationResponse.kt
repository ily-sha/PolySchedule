package com.example.polyschedule.data.network.models.registration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RegistrationResponse(val token: String) : Parcelable