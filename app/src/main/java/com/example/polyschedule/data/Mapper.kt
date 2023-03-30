package com.example.polyschedule.data

import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.UniversityEntity

class Mapper {

    fun mapUniversityEntityToBdModel(entity: UniversityEntity): UniversityDbModel {
        return UniversityDbModel(
            instituteId = entity.institute.id,
            groupId = entity.group.id,
            id = UniversityEntity.AUTOGENERATE_ID
        )
    }

    fun mapGroupEntityToGroupBdModel(group: Group): GroupDbModel{
        return GroupDbModel(
            id = group.id,
            level = group.level,
            name = group.name,
            type = group.type,
            spec = group.spec
        )
    }

    fun mapGroupBdModelToEntity(groupDbModel: GroupDbModel): Group{
        return Group(
            id = groupDbModel.id,
            level = groupDbModel.level,
            name = groupDbModel.name,
            type = groupDbModel.type,
            spec = groupDbModel.spec
        )
    }

    fun mapInstituteBdModelToEntity(instituteDbModel: InstituteDbModel): Institute{
        return Institute(
            id = instituteDbModel.id,
            abbr = instituteDbModel.abbr,
            name = instituteDbModel.name
        )
    }

    fun mapInstituteEntityToDbModel(institute: Institute): InstituteDbModel{
        return InstituteDbModel(
            id = institute.id,
            abbr = institute.abbr,
            name = institute.name
        )
    }


}