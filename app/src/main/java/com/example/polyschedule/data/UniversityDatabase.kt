package com.example.polyschedule.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [UniversityDbModel::class, InstituteDbModel::class, GroupDbModel::class],
    version = 1
)
abstract class UniversityDatabase : RoomDatabase() {

    abstract fun universityDao(): UniversityDao

    companion object {
        private var instance: UniversityDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "university.db"
        fun getInstance(application: Application): UniversityDatabase {
            instance?.let {
                return it
            }
            synchronized(LOCK) {
                instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(application, UniversityDatabase::class.java, DB_NAME)
                    .build()
                instance = db
                return db
            }

        }
    }


}