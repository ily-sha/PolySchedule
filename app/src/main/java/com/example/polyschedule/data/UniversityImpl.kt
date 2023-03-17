package com.example.polyschedule.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Group.Companion.COMMON_TYPE
import com.example.polyschedule.domain.entity.Schedule
import org.json.JSONObject
import java.net.URL
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread

object UniversityImpl: UniversityRepository {

    private var currentSchedule = MutableLiveData<MutableList<Schedule>>()
    private var scheduleOfParticularWeek = MutableLiveData<MutableList<Schedule>>()
    private val instituteLD = MutableLiveData<MutableList<Institute>>()
    private val groupLD = MutableLiveData<MutableList<Group>>()



    override fun getGroups(numberOfCourse: Int, instituteId: Int): MutableLiveData<MutableList<Group>> {
        thread {
            val groups = mutableListOf<Group>()
            try {
                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/faculty/${instituteId}/groups").readText()))!!.value
                val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("groups").getJSONObject("data").getJSONArray("${instituteId}")
                for (i in 0 until json.length()){
                    val group = Group(json[i] as JSONObject)
                    if (group.level == numberOfCourse && group.type == COMMON_TYPE) groups.add(group)
                }
                groupLD.postValue(groups.sortedBy { it.id }.toMutableList())
            } catch (e :Exception){
                println(e)
            }
        }
        return groupLD
    }

    override fun getInstitute(): LiveData<MutableList<Institute>> {
        thread {
            val instituteList = mutableListOf<Institute>()
            try {
                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/").readText()))!!.value
                val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("faculties").getJSONArray("data")
                for (i in 0 until json.length()) {
                    instituteList.add(Institute(json[i] as JSONObject))
                }
            } catch (e :Exception){
                println(e)
            }
            instituteLD.postValue(instituteList)
        }
        return instituteLD
    }

    override fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>> {
        thread {
            try {
                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL(
                    "https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId").readText()))!!.value
                currentSchedule.postValue(parsePage(result, groupId))
            } catch (e: java.lang.Exception){
                throw Exception(e)
            }
        }

        return currentSchedule
    }

    private fun parsePage(result: String, groupId: Int): MutableList<Schedule>{
        val scheduleList = mutableListOf<Schedule>()
        val json = JSONObject(result.substring(result.indexOf("{")))
        val lessons = json.getJSONObject("lessons").getJSONObject("data").getJSONArray("$groupId")
        val startDayOfWeek = json.getJSONObject("lessons").getJSONObject("week").getString("date_start")
        val (year, month, day) = startDayOfWeek.split(".").map { it.toInt() }
        val mondayOfCurrentWeek = LocalDate.of(year, month, day)
        var iterationDay = mondayOfCurrentWeek
        scheduleList.add(Schedule(JSONObject("{'date': ${iterationDay.minusDays(2)}, 'weekday': 0, 'lessons': []}"),
            mondayOfCurrentWeek))
        val intermediateScheduleList = mutableListOf<Schedule>()
        for (i in 0 until lessons.length()) {
            intermediateScheduleList.add(Schedule(lessons[i] as JSONObject, mondayOfCurrentWeek))
        }
        for (i in 1..7){
            val schedule = intermediateScheduleList.find { it.weekday == i }
            if (schedule == null) {
                scheduleList.add(Schedule(JSONObject("{'date': $iterationDay, 'weekday': $i, 'lessons': []}"),
                    mondayOfCurrentWeek))
            }
            else scheduleList.add(schedule)
            iterationDay = iterationDay.plusDays(1)
        }
        return scheduleList.sortedBy { it.weekday }.toMutableList()
    }


    override fun getSchedule(
        groupId: Int,
        instituteId: Int,
        startDate: String
    ): MutableLiveData<MutableList<Schedule>> {
        thread {
            try {

                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL(
                    "https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId?date=$startDate").readText()))!!.value
                scheduleOfParticularWeek.postValue(parsePage(result, groupId))
            } catch (e: java.lang.Exception){
                throw Exception(e)
            }
        }

        return scheduleOfParticularWeek
    }

}