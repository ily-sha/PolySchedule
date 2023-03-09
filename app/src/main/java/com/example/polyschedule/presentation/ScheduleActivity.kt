package com.example.polyschedule.presentation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.databinding.ActivityScheduleBinding
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*


class ScheduleActivity : AppCompatActivity() {


    private var course = UNDEFINED_INT
    private var instituteId = UNDEFINED_INT
    private var groupId = UNDEFINED_INT

    private lateinit var scheduleViewModel : ScheduleViewModel

    private var update: ((position: Int) -> Unit)? = null
    lateinit var a: A
    val qa = MutableLiveData<Int>()


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
        setupRvAdapter()
        scheduleViewModel.getCurrentSchedule(groupId, instituteId)
        observeSchedule()
    }

    private fun observeSchedule(){
        scheduleViewModel.currentScheduleLD?.observe(this) {
            scheduleViewPagerAdapter.scheduleList = listOf(it.last()) + it + listOf(it.first())
            setTabItemMargin()
            setCurrentTab()
        }
    }
    private fun observeParticularSchedule(){

        scheduleViewModel.scheduleOfParticularWeek?.observe(this) {
            scheduleViewPagerAdapter.scheduleList = listOf(it.last()) + it + listOf(it.first())
            setTabItemMargin()
            qa.observe(this) {
                updateDayAndMonth(it)
            }
        }
    }

    private fun setCurrentTab(){
        var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//            TODO("after 9 pm next day")
        if (currentWeekDay == 7) currentWeekDay = 1
        binding.scheduleVp.currentItem = currentWeekDay
    }

    private fun updateDayAndMonth(position: Int){
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val date = dateParser.parse(scheduleViewPagerAdapter.scheduleList[position].date)
        val dateFormatter = SimpleDateFormat("dd MMMM", Locale("ru"))
        var formattedDate = dateFormatter.format(date!!)
        if (formattedDate[0] == '0') formattedDate = formattedDate.substring(1, formattedDate.length)
        binding.dayAndMonth.text = formattedDate
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupRvAdapter() {
        scheduleViewPagerAdapter = ScheduleViewPagerAdapter(this)
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        binding.scheduleVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDayAndMonth(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                println("state, $state")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (position == 0 && positionOffset < 0.5) {
                    binding.scheduleVp.setCurrentItem(7 - 1, false)
                    val previousFirstMonday = scheduleViewPagerAdapter.scheduleList[position].previousFirstMonday.toString()
                    this@ScheduleActivity.scheduleViewModel.getScheduleOfParticularWeek(groupId, instituteId, previousFirstMonday)
                    observeParticularSchedule()
                    qa.value = 6


                }
                if (position == 6 && positionOffset > 0.5) {
                    binding.scheduleVp.setCurrentItem(1, false)
                    val nextFirstMonday = scheduleViewPagerAdapter.scheduleList[position].nextFirstMonday.toString()
                    this@ScheduleActivity.scheduleViewModel.getScheduleOfParticularWeek(groupId, instituteId, nextFirstMonday)
                    observeParticularSchedule()
                    this@ScheduleActivity.updateDayAndMonth(1)
                }
            }
        })
        bindTabLayoutWithViewPager()
    }


    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.scheduleVp
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = listOf("", "пн", "вт", "ср", "чт", "пт", "сб", "")[position]
        }.attach()

    }
    private fun setTabItemMargin(){
        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            if (i == 0 || i == 7){
               tab.visibility = View.GONE
            }
            else if (i != 1) {
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
        groupId = intent.getStringExtra(GROUP_KEY)?.toInt() ?: throw RuntimeException("GROUP_KEY params is null")
        course = intent.getStringExtra(COURSE_KEY)?.toInt() ?: throw RuntimeException("COURSE_KEY params is null")
        instituteId = intent.getStringExtra(INSTITUTE_KEY)?.toInt() ?: throw RuntimeException("INSTITUTE_KEY params is null")
    }
    interface A{
        fun update(position: Int)
    }

    companion object {
        private const val UNDEFINED_INT = -1
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