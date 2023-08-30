package com.example.polyschedule.data.network.models.group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GroupResponse(val id: Int, val name: String, val invite: String) : Parcelable