package com.example.polyschedule.data.repoimpl

import android.app.Application
import com.example.polyschedule.data.Mapper
import com.example.polyschedule.data.database.UniversityDatabase
import com.example.polyschedule.domain.repository.ScheduleRepository
import com.example.polyschedule.domain.entity.Schedule
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate


class ScheduleRepositoryImpl(private val application: Application) : ScheduleRepository {


    private val universityDao = UniversityDatabase.getInstance(application).universityDao()

    private val mapper = Mapper()



    override suspend fun getCurrentScheduleFromNetwork(
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

    override suspend fun getScheduleFromNetwork(
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


    override suspend fun addScheduleToBd(schedule: Schedule) {
        schedule.lessons
        universityDao.addSchedule(
            mapper.mapScheduleEntityToDbModel(schedule)
        )
    }

    override suspend fun getScheduleFromDb(): Schedule {
        TODO("Not yet implemented")
    }




}