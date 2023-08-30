package com.example.polyschedule.data.repoimpl

import android.app.Application
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.Mapper
import com.example.polyschedule.data.database.UniversityDatabase
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.repository.DirectionRepository

class DirectionRepositoryImpl(private val application: Application): DirectionRepository {

    private val directionDao = UniversityDatabase.getInstance(application).directionDao()
    private val mapper = Mapper()

    override fun getDirection(id: Int): Direction {
        val directionDbModel = directionDao.getDirection(id)
        val group = mapper.mapGroupBdModelToEntity(directionDao.getGroup(directionDbModel.groupId))
        val institute = mapper.mapInstituteBdModelToEntity(directionDao.getInstitute(directionDbModel.instituteId))
        return Direction(group, institute, id)
    }

    override fun getDirections(): List<Direction> {
        return directionDao.getDirections().map {
            val group = mapper.mapGroupBdModelToEntity(directionDao.getGroup(it.groupId))
            val institute =
                mapper.mapInstituteBdModelToEntity(directionDao.getInstitute(it.instituteId))
            Direction(group, institute, it.id)
        }
    }

    override fun addDirection(direction: Direction) {
        directionDao.addGroup(mapper.mapGroupEntityToGroupBdModel(direction.group))
        directionDao.addInstitute(mapper.mapInstituteEntityToDbModel(direction.institute))
        val primaryKey =
            directionDao.addDirection(mapper.mapDirectionEntityToBdModel(direction))
        CacheUtils.setString(CacheUtils.DIRECTION, primaryKey.toString(), application)
    }


}