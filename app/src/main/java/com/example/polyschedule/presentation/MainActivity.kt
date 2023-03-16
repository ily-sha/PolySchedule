package com.example.polyschedule.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.CacheUtils.Companion.COURSE_KEY
import com.example.polyschedule.data.CacheUtils.Companion.GROUP_KEY
import com.example.polyschedule.data.CacheUtils.Companion.INSTITUTE_KEY
import com.example.polyschedule.databinding.ActivityMainBinding
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.presentation.adapter.CourseAdapter
import com.example.polyschedule.presentation.adapter.GroupAdapter
import com.example.polyschedule.presentation.adapter.InstituteAdapter
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var instituteAdapter: InstituteAdapter
    private lateinit var mainViewModel: MainViewModel




    private var courseContainerHeight = 0

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw RuntimeException("ActivityMainBinding = null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkCache()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel = MainViewModel(this)
//        mainViewModel = ViewModelProvider(this)[MainViewModel(this)::class.java]
        setupRvAdapter()

        binding.appBarLayout.addOnOffsetChangedListener {appBarLayout, verticalOffset ->
            moveViews(abs(verticalOffset.toFloat()))
        }
        observeCourse()
        observeInstitute()
        obverseGroup()
        binding.courseContainer.post {
            courseContainerHeight = binding.courseContainer.height
        }
    }
    private fun checkCache(){

        CacheUtils.instance!!.apply {
            if (hasKey(COURSE_KEY, this@MainActivity) && hasKey(INSTITUTE_KEY, this@MainActivity) &&
                    hasKey(GROUP_KEY, this@MainActivity)) {
                val intent = ScheduleActivity.newIntent(this@MainActivity, getString(COURSE_KEY, this@MainActivity)!!,
                    getString(INSTITUTE_KEY, this@MainActivity)!!, getString(GROUP_KEY, this@MainActivity)!!)
                startActivity(intent)
                finish()

            }
        }
    }

    private fun obverseGroup(){
        mainViewModel.groupLD.observe(this) {
            if (binding.continueButton.visibility == View.GONE) binding.continueButton.visibility = View.VISIBLE
        }
    }

    private fun observeInstitute() {
        mainViewModel.instituteLDfromOut.observe(this) {
            instituteAdapter.instituteList = it
        }
        mainViewModel.instituteLD.observe(this) {
            val course = mainViewModel.courseLD.value
            binding.chosenTvInsitute.text = it.getAbbr()
            if (course != null) {
                uploadGroups(course, it)
            }
        }
    }

    private fun observeCourse() {
        mainViewModel.courseLD.observe(this) {
            val institute = mainViewModel.instituteLD.value
            binding.chosenTvCourse.text = it.name
            if (institute != null) {
                uploadGroups(it, institute)
            }
        }
    }


    private fun moveViews(verticalOffset: Float) {
        val tvInstituteCollapsePosition = resources.getDimension(R.dimen.vertical_margin) + courseContainerHeight
        val whenRVInstituteCollapse = tvInstituteCollapsePosition + courseContainerHeight
        binding.instituteContainer.apply {
            if (verticalOffset < tvInstituteCollapsePosition){
                this.translationY = 0f
            } else translationY = verticalOffset - tvInstituteCollapsePosition
        }
        binding.courseContainer.translationY = verticalOffset

        binding.rvCourse.apply {
            this.translationY = verticalOffset
            this.rotationX = 90 * (verticalOffset / binding.appBarLayout.totalScrollRange) * -10f
            this.visibility = if (rotationX <= -90) View.INVISIBLE else View.VISIBLE
        }
        binding.rvInstitute.apply {
            if (verticalOffset < whenRVInstituteCollapse) this.translationY = (verticalOffset / binding.appBarLayout.totalScrollRange) * -3000f
            this.rotationX = 90 * (verticalOffset / binding.appBarLayout.totalScrollRange) * 10f
            this.visibility = if (rotationX >= 90) View.INVISIBLE else View.VISIBLE
        }
        binding.chosenCourseContainer.alpha = verticalOffset / binding.appBarLayout.totalScrollRange
        binding.chosenInstituteContainer.alpha = verticalOffset / binding.appBarLayout.totalScrollRange


    }

    private fun setupRvAdapter(){
        binding.continueButton.setOnClickListener {
            CacheUtils.instance!!.apply {
                setString(GROUP_KEY, mainViewModel.groupLD.value!!.id.toString(), this@MainActivity)
                setString(INSTITUTE_KEY, mainViewModel.instituteLD.value!!.getId().toString(), this@MainActivity)
                setString(COURSE_KEY, (mainViewModel.courseLD.value!!.position + 1).toString(), this@MainActivity)
            }
            val intent = ScheduleActivity.newIntent(this, (mainViewModel.courseLD.value!!.position + 1).toString(),
                mainViewModel.instituteLD.value!!.getId().toString(), mainViewModel.groupLD.value!!.id.toString())
            startActivity(intent)
            finish()
        }
        setupCourseAdapter()
        setupInstituteAdapter()
        setupGroupAdapter()
    }

    private fun setupGroupAdapter() {
        binding.rvGroup.layoutManager = GridLayoutManager(this, 2)
        groupAdapter = GroupAdapter()
        binding.rvGroup.adapter = groupAdapter
        groupAdapter.onGroupItemClicked = {
            mainViewModel.groupLD.value = it
        }
    }

    private fun setupCourseAdapter() {
        binding.rvCourse.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val courseAdapter = CourseAdapter()
        binding.rvCourse.adapter = courseAdapter
        courseAdapter.onCourseItemClicked = {
            mainViewModel.courseLD.value = it
        }
    }

    private fun setupInstituteAdapter() {
        binding.rvInstitute.layoutManager = GridLayoutManager(this, 4, RecyclerView.HORIZONTAL, false)
        instituteAdapter = InstituteAdapter()
        binding.rvInstitute.adapter = instituteAdapter
        instituteAdapter.onInstituteItemClicked = {
            mainViewModel.instituteLD.value = it
        }
    }

    private fun uploadGroups(course: Course, institute: Institute) {
        if (binding.textViewGroup.visibility == View.GONE) {
            binding.textViewGroup.visibility = View.VISIBLE
        }
        mainViewModel.getGroups(course.position + 1, institute.getId())
        mainViewModel.groupLDfromOut.observe(this) {
            groupAdapter.groupList = it
        }
        

    }






}