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
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import androidx.viewpager2.widget.ViewPager2
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.domain.entity.Schedule
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScheduleFragment : Fragment() {
    private val TAG = "MainTr"
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
        Log.d(TAG, "currentItem ${binding.scheduleVp.currentItem}")
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(tabPosition))
    }

    private fun setupRvAdapter() {
        binding.scheduleVp.apply {
            adapter = scheduleViewPagerAdapter
            scheduleViewPagerAdapter.submitList(MutableList(8) { Schedule(it, "", listOf()) })
        }
        bindTabLayoutWithViewPager()
        setViewPagerCallback()
        binding.scheduleVp.offscreenPageLimit = 7

        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                Log.d(TAG, "onTabSelected ${tab.position}")
                binding.tabLayout.selectTab(tab)
                binding.scheduleVp.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabUnselected ${tab?.position}")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Log.d(TAG, "onTabReselected ${tab?.position}")
            }
        }
//            TabLayoutMediator.ViewPagerOnTabSelectedListener(viewPager, smoothScroll)

        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private fun setViewPagerCallback() {
        binding.scheduleVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                Log.d("MainTr", "onPageScrollStateChanged $state")
                if (binding.scheduleVp.currentItem == WeekDay.FALSE_SATURDAY.position && state == SCROLL_STATE_IDLE) {
                    binding.scheduleVp.setCurrentItem(WeekDay.SATURDAY.position, false)
                    updateDayAndMonth(WeekDay.FALSE_SATURDAY.position)
                    val previousMonday =
                        scheduleViewPagerAdapter.currentList[WeekDay.FALSE_SATURDAY.position]?.getPreviousMonday()
                            .toString()
                    loadNextOfPreviousSchedule(previousMonday)
                }
                if (binding.scheduleVp.currentItem == WeekDay.FALSE_MONDAY.position && state == SCROLL_STATE_IDLE) {
                    binding.scheduleVp.setCurrentItem(WeekDay.MONDAY.position, false)
                    updateDayAndMonth(WeekDay.FALSE_MONDAY.position)
                    val nextMonday =
                        scheduleViewPagerAdapter.currentList[WeekDay.FALSE_MONDAY.position].getNextMonday()
                            .toString()
                    loadNextOfPreviousSchedule(nextMonday)
                }
            }

            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
                updateDayAndMonth(position)
                scheduleViewModel.currentWeekDay.value =
                    WeekDay.values().find { it.position == position }
                Log.d("MainTr", position.toString())
            }
        })

    }


    private fun observeSchedule() {
        scheduleViewModel.schedule.observe(viewLifecycleOwner) { it ->
            scheduleViewPagerAdapter.submitList(it) {
                updateDayAndMonth(scheduleViewModel.currentWeekDay.value?.position ?: 0)
            }
            Log.d(TAG, "currentItem ${binding.scheduleVp.currentItem}")
            Log.d(TAG, "submitList")

        }
    }


    private fun updateDayAndMonth(position: Int) {
        val schedule = scheduleViewPagerAdapter.currentList[position]
        Log.d(TAG, "updateDayAndMonth ${schedule.lessons.size}")
        if (schedule.lessons.isNotEmpty()) {
            binding.dayAndMonth.text = scheduleViewModel.formatDate(schedule)
        }
    }


    private fun loadNextOfPreviousSchedule(startWeek: String) {
        scheduleViewModel.getScheduleOfParticularWeek(
            universityEntity.group.id, universityEntity.institute.id, startWeek
        )
    }


    private fun bindTabLayoutWithViewPager() {
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
            if (item.position != 1) {
                tab.view.post {
                    val p = tab.view.layoutParams as ViewGroup.MarginLayoutParams
                    p.setMargins(20, 0, 0, 0)
                    tab.view.requestLayout()
                }
            }
        }
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