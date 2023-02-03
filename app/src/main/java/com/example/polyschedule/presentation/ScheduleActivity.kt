package com.example.polyschedule.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.domain.Course
import com.example.polyschedule.domain.Group
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.gson.Gson
import kotlin.concurrent.thread

class ScheduleActivity : AppCompatActivity() {


    private lateinit var course: Course
    private lateinit var institute: Institute
    private lateinit var group: Group

    private lateinit var scheduleViewModel : ScheduleViewModel

    private lateinit var scheduleViewPager2: ViewPager2
    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        parseIntent()
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        initViews()
        scheduleViewModel.getCurrentSchedule(group.id, institute.getId())

        scheduleViewModel.currentScheduleLD?.observe(this) {
            if (scheduleViewPagerAdapter.lessonList.isEmpty()){
                scheduleViewPager2.adapter = scheduleViewPagerAdapter
            }
            scheduleViewPagerAdapter.lessonList = it


        }

    }

    private fun initViews(){
        scheduleViewPager2 = findViewById(R.id.schedule_vp)
        scheduleViewPagerAdapter = ScheduleViewPagerAdapter()

//        scheduleViewPagerAdapter.onItemSwipe = {
//            println(it)
//        }
        scheduleViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("onPageScrollState", state.toString())
            }
        })
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