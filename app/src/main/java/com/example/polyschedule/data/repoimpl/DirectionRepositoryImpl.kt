package com.example.polyschedule.data.repoimpl

import android.app.Application
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.Mapper
import com.example.polyschedule.data.database.UniversityDatabase
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.repository.DirectionRepository

class DirectionRepositoryImpl(private val application: Application): DirectionRepository {

    private val universityDao = UniversityDatabase.getInstance(application).universityDao()
    private val mapper = Mapper()

    override fun getGroup(id: Int): Direction {
        val universityDbModel = universityDao.getUniversity(id)
        val group =
            mapper.mapGroupBdModelToEntity(universityDao.getGroup(universityDbModel.groupId))
        val institute =
            mapper.mapInstituteBdModelToEntity(universityDao.getInstitute(universityDbModel.instituteId))
        return Direction(group, institute, id)
    }

    override fun getGroups(): List<Direction> {
        return universityDao.getAllUniversities().map {
            val group = mapper.mapGroupBdModelToEntity(universityDao.getGroup(it.groupId))
            val institute =
                mapper.mapInstituteBdModelToEntity(universityDao.getInstitute(it.instituteId))
            Direction(group, institute, it.id)
        }
    }

    override fun addGroup(direction: Direction) {
        universityDao.addGroup(mapper.mapGroupEntityToGroupBdModel(direction.group))
        universityDao.addInstitute(mapper.mapInstituteEntityToDbModel(direction.institute))
        val primaryKey =
            universityDao.addUniversity(mapper.mapUniversityEntityToBdModel(direction))
        CacheUtils.instance?.setString(CacheUtils.MAIN_GROUP, primaryKey.toString(), application)
    }

    override fun removeGroup(id: Int) {
        universityDao.deleteUniversity(id)
    }
}