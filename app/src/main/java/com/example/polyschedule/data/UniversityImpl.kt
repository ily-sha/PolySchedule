package com.example.polyschedule.data

import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.domain.Group
import com.example.polyschedule.domain.Group.Companion.COMMON_TYPE
import com.example.polyschedule.domain.Schedule
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

object UniversityImpl: UniversityRepository {

    var currentSchedule = MutableLiveData<MutableList<Schedule>>()


    override fun getGroups(numberOfCourse: Int, instituteId: Int): MutableList<Group> {
        val groups = mutableListOf<Group>()
        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/faculty/${instituteId}/groups").readText()))!!.value
            val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("groups").getJSONObject("data").getJSONArray("${instituteId}")
            for (i in 0 until json.length()){
                val group = Group(json[i] as JSONObject)
                if (group.level == numberOfCourse && group.type == COMMON_TYPE) groups.add(group)
            }

        } catch (e :Exception){
            println(e)
        }
        return groups.sortedBy { it.id }.toMutableList()
    }

    override fun getInstitute():MutableList<Institute> {
        val allInstitute = mutableListOf<Institute>()
        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/").readText()))!!.value
            val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("faculties").getJSONArray("data")
            for (i in 0 until json.length()) {
                allInstitute.add(Institute(json[i] as JSONObject))
            }
        } catch (e :Exception){
            println(e)
        }
        return allInstitute
    }

    override fun getCurrentWeekSchedule(groupId: Int, instituteId: Int): MutableLiveData<MutableList<Schedule>> {
        val scheduleList = mutableListOf<Schedule>()
        thread {
            try {
                val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/faculty/$instituteId/groups/$groupId").readText()))!!.value
                val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("lessons").getJSONObject("data").getJSONArray("$groupId")
                for (i in 0 until json.length()) {
                    scheduleList.add(Schedule(json[i] as JSONObject))

                }
                currentSchedule.postValue(scheduleList)
            } catch (e: java.lang.Exception){
                println(e)
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