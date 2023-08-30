package com.example.polyschedule.data.network.models.group

import android.os.Parcelable
import com.example.polyschedule.data.network.models.file.FileDto
import kotlinx.parcelize.Parcelize


@Parcelize
data class GroupContentResponse(val group: GroupsResponse, val homework: List<HomeworkResponse>) :
    Parcelable


@Parcelize
data class GroupsResponse(val id: Int, val name: String, val invite: String) : Parcelable

@Parcelize
data class HomeworkResponse(
    val groupId: Int,
    val subject: String,
    val description: String,
    val deadline: String,
    val creationTime: String,
    val creatorName: String,
    val editedTime: String?,
    val files: List<FileDto>
) : Parcelable