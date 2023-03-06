package com.example.polyschedule.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polyschedule.domain.UniversityRepository
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Group.Companion.COMMON_TYPE
import com.example.polyschedule.domain.entity.Schedule
import org.json.JSONObject
import java.net.URL
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