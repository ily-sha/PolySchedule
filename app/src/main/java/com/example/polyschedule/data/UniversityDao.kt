package com.example.polyschedule.data

import androidx.room.*

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

//    @Query("DELETE FROM UniversityDbModel WHERE id=:id")
//    fun addCurrentSchedule()
}