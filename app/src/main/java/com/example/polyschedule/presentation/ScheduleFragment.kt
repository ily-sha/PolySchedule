package com.example.polyschedule.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScheduleFragment : Fragment(){

    private var tabPosition = 0
    private var isFirstLoadSchedule = true

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        return layoutInflater.inflate(R.layout.schedule_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScheduleFragmentBinding.bind(view)
        setupRvAdapter(savedInstanceState)

        if (savedInstanceState == null) {
            scheduleViewModel.getCurrentWeekSchedule(
                groupId, instituteId
            )
        }
        observeSchedule()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    private fun setCurrentTab(savedInstanceState: Bundle?) {
        tabPosition = if (savedInstanceState == null) {
            scheduleViewModel.getCurrentWeekDay()
        } else {
            scheduleViewModel.currentWeekDay.value?.position ?: WeekDay.MONDAY.position
        }
        Log.d("MainTr", "current item")
//        binding.scheduleVp.post {
//            binding.scheduleVp.setCurrentItem(tabPosition, true)
//
//        }

    }

    private fun observeSchedule() {
        scheduleViewModel.currentSchedule.observe(viewLifecycleOwner) {
            scheduleViewPagerAdapter.scheduleList = it
            Log.d("MainTr", "update")
            if (isFirstLoadSchedule) {

                isFirstLoadSchedule = false
            }
        }
    }


    private fun updateDayAndMonth(position: Int) {
        val schedule = scheduleViewPagerAdapter.scheduleList[position]
        if (schedule != null) {
            binding.dayAndMonth.text = scheduleViewModel.formatDate(schedule)
        }
    }


    private fun setupRvAdapter(savedInstanceState: Bundle?) {
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        binding.scheduleVp.offscreenPageLimit = 5
        bindTabLayoutWithViewPager()
        v()
        binding.scheduleVp.setCurrentItem(3, true)

    }

    private fun v() {
        binding.scheduleVp.post {
            binding.scheduleVp.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateDayAndMonth(position)
                    scheduleViewModel.currentWeekDay.value =
                        WeekDay.values().find { it.position == position }
                    Log.d("MainTr", WeekDay.values().find { it.position == position }.toString())
                }

                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {
                    Log.d("MainTr", "$position $positionOffset")
                    if (position == WeekDay.FALSE_SATURDAY.position && positionOffset < 0.5) {
                        binding.scheduleVp.setCurrentItem(WeekDay.SATURDAY.position, false)
                        updateDayAndMonth(WeekDay.FALSE_SATURDAY.position)
                        val previousMonday =
                            scheduleViewPagerAdapter.scheduleList[position]?.previousMonday.toString()
//                    loadParticularSchedule(previousMonday)
                    }
                    if (position == WeekDay.SATURDAY.position && positionOffset > 0.5) {
                        binding.scheduleVp.setCurrentItem(WeekDay.MONDAY.position, false)
                        updateDayAndMonth(WeekDay.FALSE_MONDAY.position)
                        val nextMonday =
                            scheduleViewPagerAdapter.scheduleList[position]?.nextMonday.toString()
//                    loadParticularSchedule(nextMonday)
                    }
                }
            })
        }

    }


    private fun loadParticularSchedule(startWeek: String) {
        scheduleViewModel.getScheduleOfParticularWeek(
            groupId, instituteId, startWeek
        )
    }


    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.scheduleVp
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == WeekDay.FALSE_MONDAY.position || position == WeekDay.FALSE_SATURDAY.position) {
                tab.view.visibility = View.GONE }
            if (position != 1) {
                tab.view.post {
                    val p = tab.view.layoutParams as ViewGroup.MarginLayoutParams
                    p.setMargins(20, 0, 0, 0)
                    tab.view.requestLayout()
                }
            }
            tab.text = listOf(
                WeekDay.FALSE_SATURDAY.abbreviation,
                WeekDay.MONDAY.abbreviation,
                WeekDay.TUESDAY.abbreviation,
                WeekDay.WEDNESDAY.abbreviation,
                WeekDay.THURSDAY.abbreviation,
                WeekDay.FRIDAY.abbreviation,
                WeekDay.SATURDAY.abbreviation,
                WeekDay.FALSE_MONDAY.abbreviation
                )[position]
        }.attach()
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