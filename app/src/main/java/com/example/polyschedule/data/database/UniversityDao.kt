package com.example.polyschedule.data.database

import androidx.room.*
import com.example.polyschedule.data.database.dbmodels.GroupDbModel
import com.example.polyschedule.data.database.dbmodels.InstituteDbModel
import com.example.polyschedule.data.ScheduleDbModel
import com.example.polyschedule.data.database.dbmodels.UniversityDbModel

@Dao
interface UniversityDao {

    @Query(value = "SELECT * FROM UniversityDbModel WHERE id=:id")
    fun getUniversity(id: Int): UniversityDbModel

    @Query(value = "SELECT * FROM GroupDbModel WHERE id=:id")
    fun getGroup(id: Int): GroupDbModel

    @Query(value = "SELECT * FROM InstituteDbModel WHERE id=:id")
    fun getInstitute(id: Int): InstituteDbModel

    @Query(value = "SELECT * FROM UniversityDbModel")
    fun getAllUniversities(): List<UniversityDbModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUniversity(universityDbModel: UniversityDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInstitute(instituteDbModel: InstituteDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGroup(groupDbModel: GroupDbModel)

    @Query("DELETE FROM UniversityDbModel WHERE id=:id")
    fun deleteUniversity(id: Int)

    @Insert
    fun addSchedule(scheduleDbModel: ScheduleDbModel)

    @Query(value = "SELECT * FROM ScheduleDbModel WHERE date=:date")
    fun getSchedule(date: String): ScheduleDbModel
}