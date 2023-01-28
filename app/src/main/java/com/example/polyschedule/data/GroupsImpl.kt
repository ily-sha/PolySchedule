package com.example.polyschedule.data

import com.example.polyschedule.domain.GroupsRepository
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.domain.Speciality
import com.example.polyschedule.presentation.MainViewModel
import org.json.JSONObject
import java.net.URL

object GroupsImpl: GroupsRepository {
    val COURSE_CHOOSEN = 0
    val INSTITUTE_CHOOSEN = 0

    override fun getGroups(groups: MutableList<Speciality>) {
        try {
            val result = (Regex("""window\.__INITIAL_STATE__ = .*""").find(URL("https://ruz.spbstu.ru/faculty/${INSTITUTE_CHOOSEN}/groups").readText()))!!.value
            val json = JSONObject(result.substring(result.indexOf("{"))).getJSONObject("groups").getJSONObject("data").getJSONArray("${INSTITUTE_CHOOSEN}")
            for (i in 0 until json.length()){
                val speciality = Speciality(json[i] as JSONObject)
                if (speciality.level == COURSE_CHOOSEN) groups.add(speciality)

            }

        } catch (e :Exception){
            println(e)
        }
    }

    override fun getInstitute():List<Institute> {
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

}