package com.example.polyschedule.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polyschedule.R
import com.example.polyschedule.domain.Course
import com.example.polyschedule.domain.Group
import com.example.polyschedule.domain.Institute
import com.google.gson.Gson

class ScheduleActivity : AppCompatActivity() {


    private lateinit var course: Course
    private lateinit var institute: Institute
    private lateinit var group: Group
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        parseIntent()
    }


    private fun parseIntent(){
        if (!(intent.hasExtra(INSTITUTE_KEY) && intent.hasExtra(COURSE_KEY) && intent.hasExtra(
                GROUP_KEY
            ))) {
            throw RuntimeException("Lack more extra params")
        }
        val gson = Gson()
        group = gson.fromJson(intent.getStringExtra(GROUP_KEY), Group::class.java)
        course = gson.fromJson(intent.getStringExtra(COURSE_KEY), Course::class.java)
        institute = gson.fromJson(intent.getStringExtra(INSTITUTE_KEY), Institute::class.java)
        println(group.groupId)
        println(institute.getAbbr())
        println(course.name)
    }

    companion object {
        private const val INSTITUTE_KEY = "INSTITUTE"
        private const val COURSE_KEY = "COURSE"
        private const val GROUP_KEY = "GROUP"
        fun newIntent(context: Context,course: Course, institute: Institute, group: Group): Intent {
            val intent = Intent(context, ScheduleActivity::class.java)
            val gson = Gson()
            intent.putExtra(INSTITUTE_KEY, gson.toJson(institute))
            intent.putExtra(COURSE_KEY, gson.toJson(course))
            intent.putExtra(GROUP_KEY, gson.toJson(group))
            return intent
        }
    }

}