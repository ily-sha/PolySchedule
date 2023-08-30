package com.example.polyschedule.data.network.models.registration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RegistrationRequest(val name: String, val surname: String, val email: String): Parcelable