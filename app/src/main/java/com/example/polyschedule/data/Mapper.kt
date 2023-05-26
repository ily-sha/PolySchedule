package com.example.polyschedule.data

import com.example.polyschedule.data.database.UniversityDatabase
import com.example.polyschedule.data.database.dbmodels.GroupDbModel
import com.example.polyschedule.data.database.dbmodels.InstituteDbModel
import com.example.polyschedule.data.database.dbmodels.UniversityDbModel
import com.example.polyschedule.domain.entity.*

class Mapper {

    fun mapScheduleEntityToDbModel(schedule: Schedule): ScheduleDbModel{
        return ScheduleDbModel(
            id = UniversityDatabase.AUTOGENERATE_ID,
            weekday = schedule.weekday,
            date = schedule.date,
            lessonsId = 0
        )
    }
//    fun mapScheduleDbModelToEntity(scheduleDbModel: ScheduleDbModel): Schedule{
//        return Schedule(
//            weekday = scheduleDbModel.weekday,
//            date = scheduleDbModel.date,
//            lessons =
//        )
//    }

    fun mapLessonDbModelToEntity(lessonDbModel: LessonDbModel): Lesson {
        return Lesson(
            subject = lessonDbModel.subject,
            time_end = lessonDbModel.time_end,
            time_start =  lessonDbModel.time_start,
            typeObj = LessonType(lessonDbModel.typeObj),
            auditories = listOf(Auditorium(lessonDbModel.numberOfAuditorium, Building(lessonDbModel.buildingName))),
            teachers = listOf(Teacher(lessonDbModel.teacher))
        )
    }

    fun mapLessonEntityToDbModel(lesson: Lesson): LessonDbModel {
        return LessonDbModel(
            subject = lesson.subject,
            time_end = lesson.time_end,
            time_start =  lesson.time_start,
            typeObj = lesson.typeObj.name,
            teacher = lesson.getTeacher(),
            buildingName = lesson.auditories[0].building.name,
            numberOfAuditorium = lesson.auditories[0].name,
            id = UniversityDatabase.AUTOGENERATE_ID
        )
    }

    fun mapUniversityEntityToBdModel(entity: Direction): UniversityDbModel {
        return UniversityDbModel(
            instituteId = entity.institute.id,
            groupId = entity.group.id,
            id = Direction.AUTOGENERATE_ID
        )
    }

    fun mapGroupEntityToGroupBdModel(group: Group): GroupDbModel {
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

    fun mapInstituteEntityToDbModel(institute: Institute): InstituteDbModel {
        return InstituteDbModel(
            id = institute.id,
            abbr = institute.abbr,
            name = institute.name
        )
    }


}