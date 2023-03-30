package com.example.polyschedule.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UniversityDao {
//WHERE isMainGroup = 1 LIMIT 1
    @Query("SELECT * FROM universityDbModel")
    fun getUniversity(): UniversityDbModel

    @Insert
    fun addUniversity(universityDbModel: UniversityDbModel)


    @Insert
    fun addInstitute(instituteDbModel: InstituteDbModel)

    @Insert
    fun addGroup(groupDbModel: GroupDbModel)

    @Query("SELECT * FROM universitydbmodel WHERE instituteDbModelId = :id")
    fun getInstitute(id: Int): InstituteDbModel

    @Query("SELECT * FROM groupDbModel WHERE id=: ")
    fun getGroup(id: Int): GroupDbModel

    @Query("SELECT * FROM universitydbmodel")
    fun getAllUniversity(): List<UniversityDbModel>

    @Delete
    fun removeUniversity(universityDbModel: UniversityDbModel)

}

