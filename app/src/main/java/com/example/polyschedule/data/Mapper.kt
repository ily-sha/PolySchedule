package com.example.polyschedule.data

import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.UniversityEntity

class Mapper {


    fun mapUniversityBdModelToEntity(universityDbModel: UniversityDbModel): UniversityEntity{
        return UniversityEntity(
            group = universityDbModel.groupDbModel,
            institute = universityDbModel.instituteDbModel
        )
    }

    fun mapUniversityBdModelListToEntity(list: List<UniversityDbModel>): List<UniversityEntity>{
        return list.map {
            UniversityEntity(
                group = mapGroupBdModelToEntity(it.groupDbModel),
                institute = mapInstituteBdModelToEntity(it.instituteDbModel)
            )
        }
    }

    fun mapUniversityEntityToBdModel(entity: UniversityEntity): UniversityDbModel {
        return UniversityDbModel(
            groupDbModelId = entity.group.id,
            instituteDbModelId = entity.institute.id,
            id = UniversityEntity.DEFAULT_ID,
            isMainGroup = false
        )
    }

    fun mapEntityToGroupBdModel(group: Group): GroupDbModel{
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