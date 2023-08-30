package com.example.polyschedule.data.network.models.homework

import android.os.Parcelable
import com.example.polyschedule.data.network.models.file.FileDto
import kotlinx.parcelize.Parcelize


@Parcelize
class CreateHomeworkRequest(val token: String, val homework: HomeworkDto) : Parcelable

@Parcelize
data class HomeworkDto(val groupId: Int, val subject: String, val description: String, val deadline: Long, val creationTime: Long, val editedTime: Long, val files: List<FileDto>) :
    Parcelable
