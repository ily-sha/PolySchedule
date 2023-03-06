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
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.*
import kotlin.concurrent.thread

object UniversityImpl: UniversityRepository {

    var currentSchedule = MutableLiveData<MutableList<Schedule>>()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>> {
        val scheduleList = mutableListOf<Schedule>()
        thread {
            try {
                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL(
                    "https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId").readText()))!!.value
                val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("lessons").getJSONObject("data").getJSONArray("$groupId")
                var mondayOfCurrentWeek = LocalDate.now().with( TemporalAdjusters.previousOrSame( DayOfWeek.MONDAY))
                val intermediateScheduleList = mutableListOf<Schedule>()
                for (i in 0 until json.length()) {
                    intermediateScheduleList.add(Schedule(json[i] as JSONObject))
                }
                for (i in 1..6){
                    val element = intermediateScheduleList.find { it.weekday == i }
                    if (element == null) {
                        scheduleList.add(Schedule(JSONObject("{'date': $mondayOfCurrentWeek, 'weekday': $i, 'lessons': []}")))
                    }
                    else scheduleList.add(element)
                    mondayOfCurrentWeek = mondayOfCurrentWeek.plusDays(1)
                }

                currentSchedule.postValue(scheduleList.sortedBy { it.weekday }.toMutableList())
            } catch (e: java.lang.Exception){
                throw Exception(e)
            }
        }

        return currentSchedule
    }

    override fun getSchedule(
        groupId: Int,
        instituteId: Int,
        startDate: String
    ): MutableList<Schedule> {
        TODO("Not yet implemented")
    }

}