package com.example.polyschedule.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.domain.Course
import com.example.polyschedule.domain.Institute
import com.example.polyschedule.presentation.adapter.InstituteAdapter
import com.google.android.material.appbar.AppBarLayout
import com.example.polyschedule.presentation.adapter.CourseAdapter
import com.example.polyschedule.presentation.adapter.GroupAdapter
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.gson.Gson
import java.lang.Math.abs
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerViewGroup: RecyclerView
    private lateinit var groupAdapter: GroupAdapter
    private lateinit var recyclerViewInstitute: RecyclerView
    private lateinit var recyclerViewCourse: RecyclerView
    private lateinit var mainViewModel: MainViewModel

    private lateinit var textViewCourseInCardView: TextView
    private lateinit var textViewInstituteInCardView: TextView
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout

    private lateinit var courseCardView: CardView
    private lateinit var continueButton: CardView

    private lateinit var instituteCardView: CardView
    private lateinit var textViewCourse: TextView
    private lateinit var textViewGroup: TextView
    private lateinit var courseContainer: ConstraintLayout
    private lateinit var instituteContainer: ConstraintLayout
    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar

    private var courseContainerHeight = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews()

        appbar.addOnOffsetChangedListener {appBarLayout, verticalOffset ->
            updateViews(abs(verticalOffset.toFloat()))
        }
        observeCourse()
        observeInstitute()
        mainViewModel.groupLD.observe(this) {
            if (continueButton.visibility == View.GONE) continueButton.visibility = View.VISIBLE
        }
        courseContainer.post {
            courseContainerHeight = courseContainer.height
        }




    }

    private fun observeInstitute() {
        mainViewModel.instituteLD.observe(this) {
            val course = mainViewModel.courseLD.value
            textViewInstituteInCardView.text = it.getAbbr()
            if (course != null) {
                uploadGroups(course, it)
            }
    }
    }

    private fun observeCourse() {
        mainViewModel.courseLD.observe(this) {
            val institute = mainViewModel.instituteLD.value
            textViewCourseInCardView.text = it.name
            if (institute != null) {
                uploadGroups(it, institute)
            }

        }
    }


    private fun updateViews(verticalOffset: Float) {
        val tvInstituteCollapsePosition = resources.getDimension(R.dimen.vertical_margin) + courseContainerHeight
        val whenRVInstituteCollapse = tvInstituteCollapsePosition + courseContainerHeight
        instituteContainer.apply {
            if (verticalOffset < tvInstituteCollapsePosition){
                this.translationY = 0f
            } else translationY = verticalOffset - tvInstituteCollapsePosition
        }
        courseContainer.translationY = verticalOffset

        recyclerViewCourse.apply {
            this.translationY = verticalOffset
            this.rotationX = 90 * (verticalOffset / appbar.totalScrollRange) * -10f
            this.visibility = if (rotationX <= -90) View.INVISIBLE else View.VISIBLE
        }
        recyclerViewInstitute.apply {
            if (verticalOffset < whenRVInstituteCollapse) this.translationY = (verticalOffset / appbar.totalScrollRange) * -3000f
            this.rotationX = 90 * (verticalOffset / appbar.totalScrollRange) * 10f
            this.visibility = if (rotationX >= 90) View.INVISIBLE else View.VISIBLE
        }
        courseCardView.alpha = verticalOffset / appbar.totalScrollRange
        instituteCardView.alpha = verticalOffset / appbar.totalScrollRange


    }

    private fun initViews(){
        courseContainer = findViewById(R.id.courseContainer)
        textViewCourse = findViewById(R.id.textViewCourse)
        instituteContainer = findViewById(R.id.institute_container)
        textViewCourseInCardView = findViewById(R.id.tv_course_in_cardview)
        textViewInstituteInCardView = findViewById(R.id.textview_institute_in_cardview)
        appbar = findViewById(R.id.appBarLayout)
        toolbar = findViewById(R.id.toolbar)
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbarLayout)
        textViewGroup = findViewById(R.id.textViewGroup)
        continueButton = findViewById(R.id.cardview_continue)
        continueButton.setOnClickListener {

            startActivity(
                ScheduleActivity.newIntent(this, mainViewModel.courseLD.value!!,
                mainViewModel.instituteLD.value!!, mainViewModel.groupLD.value!!))
        }

        recyclerViewCourse = findViewById(R.id.rvCourse)
        setupCourseAdapter()

        courseCardView = findViewById(R.id.card_view_course)
        instituteCardView = findViewById(R.id.card_view_institute)

        recyclerViewInstitute = findViewById(R.id.rvInstitute)
        setupInstituteAdapter()

        recyclerViewGroup = findViewById(R.id.rvGroup)
        setupGroupAdapter()
    }

    private fun setupGroupAdapter() {
        recyclerViewGroup.layoutManager = GridLayoutManager(this, 2)
        groupAdapter = GroupAdapter()
        recyclerViewGroup.adapter = groupAdapter
        groupAdapter.onGroupItemClicked = {
            mainViewModel.groupLD.value = it
        }
    }

    private fun setupCourseAdapter() {
        recyclerViewCourse.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val courseAdapter = CourseAdapter()
        recyclerViewCourse.adapter = courseAdapter
        courseAdapter.onCourseItemClicked = {
            mainViewModel.courseLD.value = it
        }
    }

    private fun setupInstituteAdapter() {
        recyclerViewInstitute.layoutManager = GridLayoutManager(this, 4, RecyclerView.HORIZONTAL, false)
        thread {
            val institutes = mainViewModel.getInstitutes()
            runOnUiThread {
                val instituteAdapter = InstituteAdapter(institutes)
                recyclerViewInstitute.adapter = instituteAdapter
                instituteAdapter.onInstituteItemClicked = {
                    mainViewModel.instituteLD.value = it
                }
            }
        }
    }

    private fun uploadGroups(course: Course, institute: Institute) {
        if (textViewGroup.visibility == View.GONE) {
            textViewGroup.visibility = View.VISIBLE
        }
        thread {
            val groupList = mainViewModel.getGroups(course.position + 1, institute.getId())
            runOnUiThread {
                groupAdapter.groupList = groupList
            }

        }

    }






}