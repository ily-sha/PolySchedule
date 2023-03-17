package com.example.polyschedule.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class ScheduleFragment : Fragment() {

    private val LAST_POSITION = 6
    private val FIRST_POSITION = 1
    private val FALSE_FIRST_POSITION = 7
    private val FALSE_LAST_POSITION = 0

    private var _binding: ScheduleFragmentBinding? = null
    private val binding: ScheduleFragmentBinding
        get() = _binding ?: throw RuntimeException("ScheduleFragment binding is null")

    private val scheduleViewModel by lazy {
        ViewModelProvider(this)[ScheduleViewModel::class.java]
    }
    private val scheduleViewPagerAdapter by lazy {
        ScheduleViewPagerAdapter(requireContext())
    }

    private var groupId = UNDEFINED_EXTRA
    private var instituteId = UNDEFINED_EXTRA
    private var course = UNDEFINED_EXTRA


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScheduleFragmentBinding.bind(view)

        parseIntent()
        setupRvAdapter()
        scheduleViewModel.getCurrentSchedule(groupId, instituteId)
        observeSchedule()
    }

    private fun observeSchedule() {
        scheduleViewModel.currentScheduleLD?.observe(viewLifecycleOwner) {
            scheduleViewPagerAdapter.scheduleList = it
            setTabItemMargin()
            setCurrentTab()
        }
    }

    private fun observeParticularSchedule() {
        scheduleViewModel.scheduleOfParticularWeek?.observe(viewLifecycleOwner) { it ->
//TODO(wgatjkoenp)
            binding.scheduleVp.post {
                scheduleViewPagerAdapter.scheduleList = it
                setTabItemMargin()
            }

        }
    }

    private fun setCurrentTab() {
        var currentWeekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1
//            TODO("after 9 pm next day")
        if (currentWeekDay == 7) currentWeekDay = 1
        binding.scheduleVp.currentItem = currentWeekDay
    }


    private fun updateDayAndMonth(position: Int) {
        val dateParser = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val date = dateParser.parse(scheduleViewPagerAdapter.scheduleList[position].date)
        val dateFormatter = SimpleDateFormat("dd MMMM", Locale("ru"))
        var formattedDate = dateFormatter.format(date!!)
        if (formattedDate[0] == '0') formattedDate =
            formattedDate.substring(1, formattedDate.length)
        binding.dayAndMonth.text = formattedDate
    }


    private fun setupRvAdapter() {
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        bindTabLayoutWithViewPager()
        binding.scheduleVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            var isTruePosition = true
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isTruePosition) {
                    updateDayAndMonth(position)
                }
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                Log.d("viewpager", "onPageScrolled $position")
                println("$position, $positionOffset")
                isTruePosition = true
                if (position == 0 && positionOffset < 0.5) {
                    binding.scheduleVp.setCurrentItem(LAST_POSITION, false)
                    isTruePosition = false
                    updateDayAndMonth(FALSE_LAST_POSITION)
                    val previousMonday =
                        scheduleViewPagerAdapter.scheduleList[position].previousMonday.toString()
                    scheduleViewModel.getScheduleOfParticularWeek(
                        groupId, instituteId, previousMonday
                    )
                    observeParticularSchedule()

                }
                if (position == 6 && positionOffset > 0.5) {
                    binding.scheduleVp.setCurrentItem(FIRST_POSITION, false)
                    isTruePosition = false
                    updateDayAndMonth(FALSE_FIRST_POSITION)
                    val nextMonday =
                        scheduleViewPagerAdapter.scheduleList[position].nextMonday.toString()
                    scheduleViewModel.getScheduleOfParticularWeek(
                        groupId, instituteId, nextMonday
                    )
                    observeParticularSchedule()
                }
            }
        })

    }


    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.scheduleVp
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = listOf("", "пн", "вт", "ср", "чт", "пт", "сб", "")[position]
        }.attach()

    }

    private fun setTabItemMargin() {
        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = (binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            if (i == 0 || i == 7) {
                tab.visibility = View.GONE
            } else if (i != 1) {
                p.setMargins(20, 0, 0, 0)
                tab.requestLayout()
            }
        }
    }


    private fun parseIntent() {
        if (!(requireArguments().containsKey(INSTITUTE_KEY) && requireArguments().containsKey(
                COURSE_KEY
            ) && requireArguments().containsKey(GROUP_KEY))
        ) {
            throw RuntimeException("Lack more extra params")
        }
        groupId = requireArguments().getString(GROUP_KEY)?.toInt()
            ?: throw RuntimeException("GROUP_KEY params is null")
        course = requireArguments().getString(COURSE_KEY)?.toInt()
            ?: throw RuntimeException("COURSE_KEY params is null")
        instituteId = requireArguments().getString(INSTITUTE_KEY)?.toInt()
            ?: throw RuntimeException("INSTITUTE_KEY params is null")
    }


    companion object {
        private const val UNDEFINED_EXTRA = -1
        private const val INSTITUTE_KEY = "INSTITUTE"
        private const val COURSE_KEY = "COURSE"
        private const val GROUP_KEY = "GROUP"
        fun newIntent(course: String, institute: String, group: String): ScheduleFragment {
            return ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(GROUP_KEY, group)
                    putString(COURSE_KEY, course)
                    putString(INSTITUTE_KEY, institute)
                }
            }
        }
    }
}