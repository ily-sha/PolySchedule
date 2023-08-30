package com.example.polyschedule.presentation.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.viewpager.widget.ViewPager
import com.example.polyschedule.R
import com.example.polyschedule.data.network.models.schedule.LessonDto
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.MainActivity
import com.example.polyschedule.presentation.adapters.LessonAdapter
import com.example.polyschedule.presentation.adapters.ScheduleViewPagerAdapter
import com.example.polyschedule.presentation.schedule.lessoncard.LessonCardActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.tabs.TabLayout


class ScheduleFragment : Fragment(), LessonAdapter.OnLessonClicked {

    private val TAG = ScheduleFragment::class.java.toString()
    private var _binding: ScheduleFragmentBinding? = null
    private val binding: ScheduleFragmentBinding
        get() = _binding ?: throw RuntimeException("ScheduleFragment binding is null")

    private val scheduleViewModel by navGraphViewModels<ScheduleViewModel>(R.id.nav_graph)

    private val scheduleViewPagerAdapter by lazy {
        ScheduleViewPagerAdapter(this@ScheduleFragment)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = ScheduleFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        try {
            arguments?.getSerializable(DIRECTION_EXTRA)?.let {
                scheduleViewModel.direction.value = it as Direction
            } ?: throw RuntimeException("extra direction is absent")
        } catch (e: Exception) {

        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("MainTr", event.name)
            }
        })
        setupRVandTabLayout()
        observeSchedule()
        observeDirection()
        if (savedInstanceState == null) {
            scheduleViewModel.getSchedule()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        binding.moreDirection.setOnClickListener {
            showDirections()
        }
        binding.chooseDate.setOnClickListener {
            val dataPicker = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeOverlay_App_DatePicker).build()

            dataPicker.show(requireActivity().supportFragmentManager, "dataPicker")
            dataPicker.addOnPositiveButtonClickListener {
                scheduleViewModel.getSchedule(scheduleViewModel.getDateFromMillisecond(it))
            }


        }
    }

    private fun observeDirection() {
        scheduleViewModel.direction.observe(viewLifecycleOwner) {
            binding.directionGroup.text = it.group.name
            binding.directionInstitute.text = it.institute.abbr
        }
    }


    private fun showDirections() {
        val popup = PopupMenu(requireContext(), binding.directionGroup)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.direction_main, popup.menu)
        val directions = scheduleViewModel.getDirections()
        for (i in directions) {
            val viewId = i.group.id
            popup.menu?.add(0, viewId, 0, i.group.name)
        }
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.different_schedule) {
                chooseNewDirection()
            }
            val selectedDirection = directions.find { it.group.id == item.itemId }
            selectedDirection?.let {
                scheduleViewModel.setMainDirection(it)
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun chooseNewDirection() {
        scheduleViewModel.clearMainDirection()
        (requireActivity() as MainActivity).checkCache()
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }


    private fun setPage() {
        val currentDay = scheduleViewModel.currentWeekDay.value
        val tabPosition = currentDay?.position ?: scheduleViewModel.getCurrentWeekDay()
        if (binding.scheduleVp.currentItem == tabPosition) updateDayAndMonth(tabPosition)
        binding.scheduleVp.currentItem = tabPosition
    }

    private fun setupRVandTabLayout() {
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        createTabs()
        createTabListener()
        setViewPagerCallback()
        binding.scheduleVp.offscreenPageLimit = WeekDay.values().size
    }

    private fun setViewPagerCallback() {
        binding.scheduleVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Log.d(TAG, "onPageSelected ${position}")
                scheduleViewModel.currentWeekDay.value = WeekDay.values().find {
                    it.position == position
                }
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }

    private fun createTabListener() {
        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            var unselectedTab: TabLayout.Tab? = null
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "onTabSelected ${tab.position} unselectedTab ${unselectedTab?.position}")
                Log.d(TAG, (unselectedTab?.position == 0 && tab.position == 6).toString())
                if (tab.position == WeekDay.FALSE_MONDAY.position) {
                    scheduleViewModel.scheduleLD.value?.nextMonday?.let {
                        loadNextOfPreviousSchedule(it)
                        updateDayAndMonth(WeekDay.FALSE_MONDAY.position)
                        binding.scheduleVp.currentItem = WeekDay.MONDAY.position
                    }

                } else if (tab.position == WeekDay.FALSE_SATURDAY.position) {
                    scheduleViewModel.scheduleLD.value?.previousMonday?.let {
                        loadNextOfPreviousSchedule(it)
                        updateDayAndMonth(WeekDay.FALSE_SATURDAY.position)
                        binding.scheduleVp.currentItem = WeekDay.SATURDAY.position
                    }
                } else if (unselectedTab?.position == 0 && tab.position == 6) {
                    return
                } else if (unselectedTab?.position == 7 && tab.position == 1) {
                    return
                } else if (tab.position == binding.scheduleVp.currentItem) {
                    updateDayAndMonth(tab.position)
                } else if (tab.position != binding.scheduleVp.currentItem) {
                    updateDayAndMonth(tab.position)
                    binding.scheduleVp.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                unselectedTab = tab
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        }
        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)

    }

    private fun loadNextOfPreviousSchedule(startWeek: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.scheduleVp.visibility = View.INVISIBLE
        scheduleViewModel.getSchedule(startWeek)
    }

    private fun observeSchedule() {
        scheduleViewModel.scheduleLD.observe(viewLifecycleOwner) {
            Log.d(TAG, "observeSchedule ${it.schedule.size}")
            scheduleViewPagerAdapter.scheduleList = it.schedule.toMutableList()
            setPage()
            binding.progressBar.visibility = View.INVISIBLE
            binding.scheduleVp.visibility = View.VISIBLE
        }
    }


    private fun updateDayAndMonth(position: Int) {
        val scheduleList = scheduleViewModel.scheduleLD.value
        if (scheduleList != null) {
            Log.d(TAG, "updateDayAndMonth ${scheduleList.schedule[position].date}")
            binding.dayAndMonth.text = scheduleList.schedule[position].date
        }

    }


    private fun createTabs() {
        Log.d(TAG, "createTabs")
        val list = listOf(
            WeekDay.FALSE_SATURDAY,
            WeekDay.MONDAY,
            WeekDay.TUESDAY,
            WeekDay.WEDNESDAY,
            WeekDay.THURSDAY,
            WeekDay.FRIDAY,
            WeekDay.SATURDAY,
            WeekDay.FALSE_MONDAY
        )
        for (item in list) {
            val tab: TabLayout.Tab = binding.tabLayout.newTab()
            tab.text = item.abbreviation
            binding.tabLayout.addTab(tab, item.position)

            if (item == WeekDay.FALSE_MONDAY || item == WeekDay.FALSE_SATURDAY) {
                tab.view.visibility = View.GONE
            }
            if (item.position != WeekDay.MONDAY.position) {
                tab.view.post {
                    val p = tab.view.layoutParams as ViewGroup.MarginLayoutParams
                    p.setMargins(20, 0, 0, 0)
                    tab.view.requestLayout()
                }
            }
        }
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1))
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView")
        binding.tabLayout.clearOnTabSelectedListeners()
        binding.scheduleVp.clearOnPageChangeListeners()
        super.onDestroyView()
        _binding = null
    }

    override fun onLessonClick(lesson: LessonDto) {
        findNavController().navigate(
            R.id.action_scheduleFragment_to_lessonCardActivity,
            LessonCardActivity.newInstance(lesson)
        )
    }

    companion object {
        const val DIRECTION_EXTRA = "direction"
    }

}