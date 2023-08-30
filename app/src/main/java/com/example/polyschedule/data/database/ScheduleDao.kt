package com.example.polyschedule.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.polyschedule.data.ScheduleDbModel


@Dao
interface ScheduleDao {

    @Insert
    fun addSchedule(scheduleDbModel: ScheduleDbModel)

    @Query(value = "SELECT * FROM ScheduleDbModel WHERE date=:date")
    fun getSchedule(date: String): ScheduleDbModel
}