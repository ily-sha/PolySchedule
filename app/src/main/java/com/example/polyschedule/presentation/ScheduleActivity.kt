package com.example.polyschedule.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.domain.Course
import com.example.polyschedule.domain.Group
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class ScheduleActivity : AppCompatActivity() {


    private lateinit var course: Course
    private lateinit var institute: Institute
    private lateinit var group: Group

    private lateinit var scheduleViewModel : ScheduleViewModel

    private lateinit var scheduleViewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout

    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        parseIntent()
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        initViews()
        scheduleViewModel.getCurrentSchedule(group.id, institute.getId())

        scheduleViewModel.currentScheduleLD?.observe(this) {
            scheduleViewPagerAdapter.scheduleList = it
            setTabItemMargin()

        }

    }

    private fun initViews(){
        scheduleViewPager2 = findViewById(R.id.schedule_vp)
        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(this)
        scheduleViewPager2.adapter = scheduleViewPagerAdapter
        tabLayout = findViewById(R.id.tab_layout)
        bindTabLayoutWithViewPager()

    }

    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            tabLayout, scheduleViewPager2
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = listOf("пн", "вт", "ср", "чт", "пт", "сб")[position]
        }.attach()

    }
    private fun setTabItemMargin(){
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            if (i != 0) {
                p.setMargins(20, 0, 0, 0)
                tab.requestLayout()
            }
        }
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