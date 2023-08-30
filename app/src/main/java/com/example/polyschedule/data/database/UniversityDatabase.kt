package com.example.polyschedule.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.polyschedule.data.database.dbmodels.GroupDbModel
import com.example.polyschedule.data.database.dbmodels.InstituteDbModel
import com.example.polyschedule.data.LessonDbModel
import com.example.polyschedule.data.ScheduleDbModel
import com.example.polyschedule.data.database.dbmodels.DirectionDbModel

@Database(
    entities = [DirectionDbModel::class, InstituteDbModel::class, GroupDbModel::class, ScheduleDbModel::class, LessonDbModel::class],
    version = 2
)
abstract class UniversityDatabase : RoomDatabase() {

    abstract fun directionDao(): DirectionDao

    abstract fun scheduleDao(): ScheduleDao


    companion object {
        const val AUTOGENERATE_ID = 0
        private var instance: UniversityDatabase? = null
        private const val name = "university.db"
        private val LOCK = Any()

        fun getInstance(application: Application): UniversityDatabase {

            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                instance = Room.databaseBuilder(application, UniversityDatabase::class.java, name)
                    .allowMainThreadQueries().build()
                return instance as UniversityDatabase
            }
        }
    }


}