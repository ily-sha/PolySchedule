package com.example.polyschedule.data.network.models.group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class JoinToGroupRequest(val token: String, val invite: String) : Parcelable