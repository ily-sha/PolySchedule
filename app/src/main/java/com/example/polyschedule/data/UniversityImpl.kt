package com.example.polyschedule.data

import android.app.Application
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Group.Companion.COMMON_TYPE
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.UniversityEntity
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate


class UniversityImpl(private val application: Application) : UniversityRepository {


    private val universityDao = UniversityDatabase.getInstance(application).universityDao()

    private val mapper = Mapper()


    override suspend fun getGroups(
        numberOfCourse: Int,
        instituteId: Int
    ): MutableMap<String, MutableList<Group>> {
        val groupsList = mutableListOf<Group>()
        try {
            val result =
                (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/faculty/${instituteId}/groups").readText()))!!.value
            val json = JSONObject(result.substring(result.indexOf("{")))
            val groups = json.getJSONObject("groups").getJSONObject("data")
                .getJSONArray("$instituteId")
            for (i in 0 until groups.length()) {
                val group = Gson().fromJson(groups[i].toString(), Group::class.java)
                if (group.level == numberOfCourse && group.type == COMMON_TYPE) groupsList.add(
                    group
                )
            }
            val mutableMap = groupsList.sortedBy { it.id }
                .groupBy { it.spec } as MutableMap<String, MutableList<Group>>
            return mutableMap.apply {
                if (keys.contains("")) {
                    remove("")?.let {
                        if (this.isEmpty()) {
                            put("Все группы", it)
                        } else {
                            put("Остальные группы", it)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getInstitute(): MutableList<Institute> {
        val instituteList = mutableListOf<Institute>()
        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(
                URL("https://ruz.spbstu.ru/")
                    .readText()
            ))!!.value
            val json = JSONObject(result.substring(result.indexOf("{")))
            val institutes = json.getJSONObject("faculties").getJSONArray("data")
            for (i in 0 until institutes.length()) {
                val institute = Gson().fromJson(institutes[i].toString(), Institute::class.java)
                instituteList.add(institute)
            }
            return instituteList
        } catch (e: Exception) {
            throw e
        }


    }

    override suspend fun getCurrentWeekSchedule(
        groupId: Int,
        instituteId: Int
    ): MutableList<Schedule> {

        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(
                URL(
                    "https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId"
                ).readText()
            ))!!.value
            return parsePage(result, groupId)
        } catch (e: java.lang.Exception) {
            throw e
        }


    }

    private fun parsePage(result: String, groupId: Int): MutableList<Schedule> {
        val scheduleList = mutableListOf<Schedule>()
        val json = JSONObject(result.substring(result.indexOf("{")))
        val lessons = json.getJSONObject("lessons").getJSONObject("data").getJSONArray("$groupId")
        val startDayOfWeek =
            json.getJSONObject("lessons").getJSONObject("week").getString("date_start")
        val (year, month, day) = startDayOfWeek.split(".").map { it.toInt() }
        val mondayOfCurrentWeek = LocalDate.of(year, month, day)
        addFalseMondayAndSaturday(scheduleList, mondayOfCurrentWeek)
        val loadedScheduleList = mutableListOf<Schedule>()
        for (i in 0 until lessons.length()) {
            val schedule = Gson().fromJson(lessons[i].toString(), Schedule::class.java).apply {
                init(mondayOfCurrentWeek)
            }
            loadedScheduleList.add(schedule)
        }
        mergeBlankAndLoadedSchedule(scheduleList, loadedScheduleList, mondayOfCurrentWeek)
        return scheduleList.sortedBy { it.weekday }.toMutableList()
    }

    private fun mergeBlankAndLoadedSchedule(
        scheduleList: MutableList<Schedule>,
        intermediateScheduleList: MutableList<Schedule>,
        mondayOfCurrentWeek: LocalDate
    ) {
        var iterationDay = mondayOfCurrentWeek
        for (i in 1..6) {
            val loadedSchedule = intermediateScheduleList.find { it.weekday == i }
            if (loadedSchedule == null) {
                val blank = "{'date': $iterationDay, 'weekday': $i, 'lessons': []}"
                val blankSchedule = Gson().fromJson(blank, Schedule::class.java)
                blankSchedule.init(mondayOfCurrentWeek)
                scheduleList.add(blankSchedule)
            } else scheduleList.add(loadedSchedule)
            iterationDay = iterationDay.plusDays(1)
        }

    }

    private fun addFalseMondayAndSaturday(
        scheduleList: MutableList<Schedule>,
        mondayOfCurrentWeek: LocalDate
    ) {
        for (i in 0..1) {
            val blank =
                if (i == 0) "{'date': ${mondayOfCurrentWeek.minusDays(2)}, " +
                        "'weekday': 0, 'lessons': []}" else
                    "{'date': ${
                        mondayOfCurrentWeek.plusDays(7)
                    }, 'weekday': 7, 'lessons': []}"
            val blankSchedule = Gson().fromJson(blank, Schedule::class.java)
            blankSchedule.init(mondayOfCurrentWeek)
            scheduleList.add(blankSchedule)
        }
    }


    override suspend fun getSchedule(
        groupId: Int,
        instituteId: Int,
        startDate: String
    ): MutableList<Schedule> {
        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(
                URL(
                    "https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId?date=$startDate"
                ).readText()
            ))!!.value
            return parsePage(result, groupId)
        } catch (e: java.lang.Exception) {
            throw e
        }
    }


    override fun getUniversity(id: Int): UniversityEntity {
        val universityDbModel = universityDao.getUniversity(id)
        val group =
            mapper.mapGroupBdModelToEntity(universityDao.getGroup(universityDbModel.groupId))
        val institute =
            mapper.mapInstituteBdModelToEntity(universityDao.getInstitute(universityDbModel.instituteId))
        return UniversityEntity(group, institute, id)
    }

    override fun getAllUniversity(): List<UniversityEntity> {
        return universityDao.getAllUniversities().map {
            val group = mapper.mapGroupBdModelToEntity(universityDao.getGroup(it.groupId))
            val institute =
                mapper.mapInstituteBdModelToEntity(universityDao.getInstitute(it.instituteId))
            UniversityEntity(group, institute, it.id)
        }
    }

    override fun addUniversity(universityEntity: UniversityEntity) {
        universityDao.addGroup(mapper.mapGroupEntityToGroupBdModel(universityEntity.group))
        universityDao.addInstitute(mapper.mapInstituteEntityToDbModel(universityEntity.institute))
        val primaryKey =
            universityDao.addUniversity(mapper.mapUniversityEntityToBdModel(universityEntity))
        CacheUtils.instance?.setString(CacheUtils.MAIN_GROUP, primaryKey.toString(), application)
    }

    override fun removeUniversity(id: Int) {
        universityDao.deleteUniversity(id)
    }

    override fun addSchedule(schedule: Schedule) {
        schedule.lessons
        universityDao.addSchedule(
            mapper.mapScheduleEntityToDbModel(schedule)
        )
    }

    override fun getSchedule(date: String): Schedule {
        TODO("Not yet implemented")
    }


}