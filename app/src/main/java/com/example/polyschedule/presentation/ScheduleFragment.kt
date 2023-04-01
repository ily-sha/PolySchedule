package com.example.polyschedule.presentation

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScheduleFragment : Fragment() {

    private var _binding: ScheduleFragmentBinding? = null
    private val binding: ScheduleFragmentBinding
        get() = _binding ?: throw RuntimeException("ScheduleFragment binding is null")

    private val scheduleViewModel by lazy {
        ViewModelProvider(this)[ScheduleViewModel::class.java]
    }
    private val scheduleViewPagerAdapter by lazy {
        ScheduleViewPagerAdapter(requireContext())
    }


    private lateinit var universityEntity: UniversityEntity


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.schedule_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        parseIntent()
        _binding = ScheduleFragmentBinding.bind(view)
        setupRvAdapter()
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("MainTr", event.name)
            }

        })
        setCurrentTab(savedInstanceState)
        if (savedInstanceState == null) {
            scheduleViewModel.getCurrentWeekSchedule(universityEntity)
        }
        observeSchedule()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        binding.menu.setOnClickListener { menuClicked(it) }
    }

    private fun menuClicked(view: View) {
        val popup = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main, popup.menu)
        popup.show()
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.change_group -> clickChangeGroup(item)
                R.id.add_new_group -> addNewGroup()
            }
            groupChanged(item)
            false
        }
    }

    private fun groupChanged(item: MenuItem) {
        scheduleViewModel.getAllUniversities().let { it ->
            val universityEntity = it.find { it.group.id == item.itemId }
            universityEntity?.let {
                scheduleViewModel.changeMainGroup(universityEntity)
                scheduleViewModel.getCurrentWeekSchedule(universityEntity)
            }
        }
    }

    private fun addNewGroup(): Boolean {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container, ChooseAttributeFragment.newIntent()
        ).commit()
        return true
    }

    private fun clickChangeGroup(menuItem: MenuItem): Boolean {
        val submenu = menuItem.subMenu
        val list = scheduleViewModel.getAllUniversities()
        for (i in list) {
            val viewId = i.group.id
            submenu?.add(0, viewId, 0, i.group.name)
            val menuItem = submenu?.findItem(viewId)
            val icon = if (i.group.id == universityEntity.group.id) {
                R.drawable.radio_button_checked
            } else R.drawable.radio_button_unchecked
            menuItem?.setIcon(icon)
        }

        return true
    }


    private fun setCurrentTab(savedInstanceState: Bundle?) {
        val tabPosition = if (savedInstanceState == null) {
            scheduleViewModel.getCurrentWeekDay()
        } else {
            scheduleViewModel.currentWeekDay.value?.position ?: WeekDay.MONDAY.position
        }
        binding.scheduleVp.currentItem = tabPosition
    }

    private fun setupRvAdapter() {
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        bindTabLayoutWithViewPager()
        setViewPagerCallback()
    }

    private fun setViewPagerCallback() {
        binding.scheduleVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDayAndMonth(position)
                scheduleViewModel.currentWeekDay.value =
                    WeekDay.values().find { it.position == position }
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                if (scheduleViewModel.schedule.value != null) {
                    if (position == WeekDay.FALSE_SATURDAY.position && positionOffset < 0.5) {
                        binding.scheduleVp.setCurrentItem(WeekDay.SATURDAY.position, false)
                        updateDayAndMonth(WeekDay.FALSE_SATURDAY.position)
                        val previousMonday =
                            scheduleViewPagerAdapter.scheduleList[position]?.getPreviousMonday().toString()
                        loadNextOfPreviousSchedule(previousMonday)
                    }
                    if (position == WeekDay.SATURDAY.position && positionOffset > 0.5) {
                        binding.scheduleVp.setCurrentItem(WeekDay.MONDAY.position, false)
                        updateDayAndMonth(WeekDay.FALSE_MONDAY.position)
                        val nextMonday =
                            scheduleViewPagerAdapter.scheduleList[position]?.getNextMonday().toString()
                        loadNextOfPreviousSchedule(nextMonday)
                    }
                }
            }
        })
    }


    private fun observeSchedule() {
        scheduleViewModel.schedule.observe(viewLifecycleOwner) { it ->
            scheduleViewPagerAdapter.scheduleList = it
            val currentWeekDay = scheduleViewModel.currentWeekDay.value
            if (currentWeekDay != null) {
                updateDayAndMonth(currentWeekDay.position)
            }
        }
    }


    private fun updateDayAndMonth(position: Int) {
        val schedule = scheduleViewPagerAdapter.scheduleList[position]
        if (schedule != null) {
            binding.dayAndMonth.text = scheduleViewModel.formatDate(schedule)
        }
//        Log.d("MainTr", "binding.dayAndMonth.text")
    }


    private fun loadNextOfPreviousSchedule(startWeek: String) {
        scheduleViewModel.getScheduleOfParticularWeek(
            universityEntity.group.id, universityEntity.institute.id, startWeek
        )
    }


    private fun bindTabLayoutWithViewPager() {
        TabLayoutMediator(
            binding.tabLayout, binding.scheduleVp
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == WeekDay.FALSE_MONDAY.position || position == WeekDay.FALSE_SATURDAY.position) {
                tab.view.visibility = View.GONE
            }
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
        if (!(requireArguments().containsKey(UNIVERSITY_KEY) || requireArguments().containsKey(
                UNIVERSITY_ID_KEY
            ))
        ) throw RuntimeException("Lack more extra params")
        if (requireArguments().containsKey(UNIVERSITY_KEY)) {
            requireArguments().getSerializable(UNIVERSITY_KEY).let {
                if (it == null) throw RuntimeException("UNIVERSITY_KEY params is null")
                universityEntity = it as UniversityEntity
            }
        }
        if (requireArguments().containsKey(UNIVERSITY_ID_KEY)) {
            requireArguments().getInt(UNIVERSITY_ID_KEY).let {
                universityEntity = scheduleViewModel.getCurrentUniversity(it)
            }
        }

    }


    companion object {

        private const val UNDEFINED_EXTRA = -1
        const val UNIVERSITY_KEY = "UNIVERSITY"
        const val UNIVERSITY_ID_KEY = "UNIVERSITY_ID"


        fun newIntent(universityEntity: UniversityEntity): ScheduleFragment {
            return ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(UNIVERSITY_KEY, universityEntity)
                }
            }
        }

        fun newIntent(universityEntityId: Int): ScheduleFragment {
            return ScheduleFragment().apply {
                arguments = Bundle().apply {
                    putInt(UNIVERSITY_ID_KEY, universityEntityId)
                }
            }
        }
    }

}