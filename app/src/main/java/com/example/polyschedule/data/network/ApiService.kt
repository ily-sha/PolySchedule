package com.example.polyschedule.data.network

import com.example.polyschedule.data.network.models.emailconfirmation.EmailConfirmResponse
import com.example.polyschedule.data.network.models.emailconfirmation.EmailConfirmationRequest
import com.example.polyschedule.data.network.models.emailconfirmation.Student
import com.example.polyschedule.data.network.models.group.CreateGroupRequest
import com.example.polyschedule.data.network.models.group.GroupContentResponse
import com.example.polyschedule.data.network.models.group.GroupResponse
import com.example.polyschedule.data.network.models.group.JoinToGroupRequest
import com.example.polyschedule.data.network.models.homework.CreateHomeworkRequest
import com.example.polyschedule.data.network.models.login.LoginRequest
import com.example.polyschedule.data.network.models.registration.RegistrationRequest
import com.example.polyschedule.data.network.models.schedule.ScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<String>


    @POST("register")
    suspend fun register(@Body registrationRequest: RegistrationRequest): Response<String>

    @POST("confirm")
    suspend fun emailConfirm(@Body emailConfirmationRequest: EmailConfirmationRequest): Response<EmailConfirmResponse>


    @POST("group")
    suspend fun createGroup(@Body createGroupRequest: CreateGroupRequest): Response<GroupResponse>

    @POST("group/join")
    suspend fun joinToGroup(@Body joinToGroupRequest: JoinToGroupRequest): Response<GroupResponse>

    @GET("student/{token}")
    suspend fun getStudentInfo(@Path("token") token: String): Response<Student>

    @GET("group/{groupId}")
    suspend fun getGroupContent(@Path("groupId") groupId: Int): Response<GroupContentResponse>


    @POST("homework")
    suspend fun createHomework(@Body createHomeworkRequest: CreateHomeworkRequest): Response<Int>

    @GET("schedule")
    suspend fun getSchedule(
        @Query("token") token: String? = null,
        @Query("groupId") groupId: String,
        @Query("instituteId") instituteId: String,
        @Query("startDate") startDate: String? = null
    ): Response<ScheduleResponse>
}

