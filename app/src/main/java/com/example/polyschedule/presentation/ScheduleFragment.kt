package com.example.polyschedule.presentation

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ScheduleFragmentBinding
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.domain.entity.WeekDay
import com.example.polyschedule.presentation.adapter.ScheduleViewPagerAdapter
import com.google.android.material.tabs.TabLayout


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

    private val universityEntity: UniversityEntity by lazy {
        arg.universityEntity
    }

    private val arg by lazy {
        navArgs<ScheduleFragmentArgs>().value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.schedule_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = ScheduleFragmentBinding.bind(view)
        setupRVandTabLayout()

        if (savedInstanceState == null) {
            scheduleViewModel.getCurrentWeekSchedule(universityEntity)
        }
        observeSchedule()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
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
            subMenuGroupClicked(item)
            false
        }
    }

    private fun subMenuGroupClicked(item: MenuItem) {
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
            R.id.main_fragment_container, InstituteSettingFragment.newIntent()
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


    private fun setPage() {
        val currentDay = scheduleViewModel.currentWeekDay.value
        val tabPosition = currentDay?.position ?: scheduleViewModel.getCurrentWeekDay()
        binding.scheduleVp.currentItem = tabPosition
    }

    private fun setupRVandTabLayout() {
        binding.scheduleVp.adapter = scheduleViewPagerAdapter
        createTabs()
        addTabListener()
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
                scheduleViewModel.currentWeekDay.value = WeekDay.values().find {
                    it.position == position
                }
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })


    }

    private fun addTabListener() {
        val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
            var unselectedTab: TabLayout.Tab? = null
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == WeekDay.FALSE_MONDAY.position) {
                    val nextMonday =
                        scheduleViewPagerAdapter.scheduleList[WeekDay.FALSE_MONDAY.position].getNextMonday()
                            .toString()
                    loadNextOfPreviousSchedule(nextMonday)
                    updateDayAndMonth(WeekDay.FALSE_MONDAY.position)
                    binding.scheduleVp.currentItem = WeekDay.MONDAY.position
                } else if (tab.position == WeekDay.FALSE_SATURDAY.position) {
                    val previousMonday =
                        scheduleViewPagerAdapter.scheduleList[WeekDay.FALSE_SATURDAY.position].getPreviousMonday()
                            .toString()
                    loadNextOfPreviousSchedule(previousMonday)
                    updateDayAndMonth(WeekDay.FALSE_SATURDAY.position)
                    binding.scheduleVp.currentItem = WeekDay.SATURDAY.position
                } else if (tab.position == binding.scheduleVp.currentItem) {
                    updateDayAndMonth(tab.position)
                } else if (tab.position != binding.scheduleVp.currentItem) {
                    updateDayAndMonth(tab.position)
                    binding.scheduleVp.currentItem = tab.position
                } else if (unselectedTab?.position == 0 && tab.position == 6) {
                    return
                } else if (unselectedTab?.position == 7 && tab.position == 1) {
                    return
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                unselectedTab = tab
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
        binding.tabLayout.addOnTabSelectedListener(onTabSelectedListener)

    }

    private fun loadNextOfPreviousSchedule(startWeek: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.scheduleVp.visibility = View.INVISIBLE
        scheduleViewModel.getScheduleOfParticularWeek(
            universityEntity.group.id, universityEntity.institute.id, startWeek
        )
    }

    private fun observeSchedule() {
        scheduleViewModel.scheduleLD.observe(viewLifecycleOwner) { it ->
            scheduleViewPagerAdapter.scheduleList = it
            setPage()
            binding.progressBar.visibility = View.INVISIBLE
            binding.scheduleVp.visibility = View.VISIBLE
        }
    }


    private fun updateDayAndMonth(position: Int) {
        if (scheduleViewPagerAdapter.scheduleList.isNotEmpty()) {
            val schedule = scheduleViewPagerAdapter.scheduleList[position]
            binding.dayAndMonth.text = scheduleViewModel.formatDate(schedule)
        }

    }


    private fun createTabs() {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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