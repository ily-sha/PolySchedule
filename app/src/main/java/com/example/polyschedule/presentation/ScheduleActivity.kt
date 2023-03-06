package com.example.polyschedule.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.databinding.ActivityScheduleBinding
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class ScheduleActivity : AppCompatActivity() {


    private lateinit var course: String
    private lateinit var institute: String
    private lateinit var group: String

    private lateinit var scheduleViewModel : ScheduleViewModel


    private lateinit var scheduleViewPagerAdapter: ScheduleViewPagerAdapter

    private var _binding: ActivityScheduleBinding? = null
    private val binding: ActivityScheduleBinding
        get() = _binding ?: throw RuntimeException("ActivityScheduleBinding = null")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityScheduleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        scheduleViewModel = ViewModelProvider(this)[ScheduleViewModel::class.java]
        observeSchedule()
        setupRvAdapter()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeSchedule(){
        scheduleViewModel.getCurrentSchedule(group.toInt(), institute.toInt())
        scheduleViewModel.currentScheduleLD?.observe(this) {
            scheduleViewPagerAdapter.scheduleList = it
            setTabItemMargin()
            var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
            val currentDay = Calendar.getInstance().get(Calendar.DATE)

//            TODO("after 9 pm next day")
            if (currentWeekDay == 7) currentWeekDay = 1
            binding.scheduleVp.currentItem = currentWeekDay
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateDayAndMonth(){
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse("" , pattern)
        binding.dayAndMonth.text = "${date.dayOfMonth} ${date.month}"
    }
    private fun setupRvAdapter(){
        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(this)

        binding.scheduleVp.adapter = scheduleViewPagerAdapter

        binding.scheduleVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                println(position)
                    updateDayAndMonth()
            }
        })
        bindTabLayoutWithViewPager()
    }

    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.scheduleVp
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = listOf("пн", "вт", "ср", "чт", "пт", "сб")[position]
        }.attach()

    }
    private fun setTabItemMargin(){
        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
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
        group = intent.getStringExtra(GROUP_KEY) ?: throw RuntimeException("GROUP_KEY params is null")
        course = intent.getStringExtra(COURSE_KEY) ?: throw RuntimeException("COURSE_KEY params is null")
        institute = intent.getStringExtra(INSTITUTE_KEY) ?: throw RuntimeException("INSTITUTE_KEY params is null")

    }

    companion object {

        private const val INSTITUTE_KEY = "INSTITUTE"
        private const val COURSE_KEY = "COURSE"
        private const val GROUP_KEY = "GROUP"
        fun newIntent(context: Context, course: String, institute: String, group: String): Intent {
            val intent = Intent(context, ScheduleActivity::class.java)
            intent.putExtra(INSTITUTE_KEY, institute)
            intent.putExtra(COURSE_KEY, course)
            intent.putExtra(GROUP_KEY, group)
            return intent
        }
    }

}