package com.example.polyschedule.data.database

import androidx.room.*
import com.example.polyschedule.data.database.dbmodels.GroupDbModel
import com.example.polyschedule.data.database.dbmodels.InstituteDbModel
import com.example.polyschedule.data.database.dbmodels.DirectionDbModel

@Dao
interface DirectionDao {

    @Query(value = "SELECT * FROM DirectionDbModel WHERE id=:id")
    fun getDirection(id: Int): DirectionDbModel

    @Query(value = "SELECT * FROM DirectionDbModel")
    fun getDirections(): List<DirectionDbModel>

    @Query(value = "SELECT * FROM GroupDbModel WHERE id=:id")
    fun getGroup(id: Int): GroupDbModel

    @Query(value = "SELECT * FROM InstituteDbModel WHERE id=:id")
    fun getInstitute(id: Int): InstituteDbModel




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addDirection(directionDbModel: DirectionDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addInstitute(instituteDbModel: InstituteDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addGroup(groupDbModel: GroupDbModel)




}