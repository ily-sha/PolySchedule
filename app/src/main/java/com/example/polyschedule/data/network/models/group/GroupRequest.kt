package com.example.polyschedule.data.network.models.group

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateGroupRequest(val token: String, val groupDto: GroupDto) : Parcelable


@Parcelize
data class GroupDto(val name: String) : Parcelable