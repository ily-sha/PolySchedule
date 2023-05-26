package com.example.polyschedule.data.repoimpl

import com.example.polyschedule.domain.entity.Group
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.repository.ScheduleSettingRepository
import com.google.gson.Gson
import org.json.JSONObject
import java.net.URL

class ScheduleSettingRepositoryImpl: ScheduleSettingRepository {


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
                if (group.level == numberOfCourse && group.type == Group.COMMON_TYPE) groupsList.add(
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
}