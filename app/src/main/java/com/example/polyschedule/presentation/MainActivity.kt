package com.example.polyschedule.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.presentation.adapter.GroupAdapter
import com.example.polyschedule.R
import com.example.polyschedule.domain.Speciality
import com.example.polyschedule.presentation.adapter.InstituteAdapter
import com.google.android.material.appbar.AppBarLayout
import com.example.polyschedule.presentation.MainViewModel.Companion.coursesList
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {



    var allGroups = mutableListOf<Speciality>()
    lateinit var recyclerViewGroup: RecyclerView
    lateinit var groupAdapter: GroupAdapter
    lateinit var speciality: TextView
    private val viewsOfCourse = mutableListOf<Pair<TextView, View>>()
    lateinit var recyclerViewInstitute: RecyclerView
    private lateinit var mainViewModel: MainViewModel





    private lateinit var linearLayoutCourse: LinearLayout
    private lateinit var textViewInstitute: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar

    private var tvCourseHeight = 0




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews()

        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            updateViews(abs(verticalOffset))
            }

        mainViewModel.course.observe(this) {

        }
        mainViewModel.institute.observe(this) {

        }
        textViewCourse.post {
            tvCourseHeight = textViewCourse.height
        }

    }






    private fun updateViews(verticalOffset: Int) {
        val const = 2 * resources.getDimension(R.dimen.vertical_margin) + tvCourseHeight
        println("verticalOffset: $verticalOffset")
        textViewInstitute.apply {
            if (verticalOffset < const){
                this.translationY = 0f
            } else translationY = verticalOffset.toFloat() - const

        }
        textViewCourse.apply {
            this.translationY = verticalOffset.toFloat()
        }
        linearLayoutCourse.apply {
            this.translationX = verticalOffset * 8f
        }
        recyclerViewInstitute.apply {

            this.translationX = verticalOffset * -4f
            this.translationY = verticalOffset.toFloat()
        }
        val courseItem = mainViewModel.course.value
        if (courseItem != null) {
            val view = viewsOfCourse[courseItem.position].second
            view.apply {
                this.translationX = verticalOffset * 5f
                this.translationY = verticalOffset.toFloat()
            }
        }

    }

    private fun initViews(){
        textViewCourse = findViewById(R.id.textViewCourse)
        textViewInstitute = findViewById(R.id.textViewInstitute)
        appbar = findViewById(R.id.appBarLayout)
        toolbar = findViewById(R.id.toolbar)

        linearLayoutCourse = findViewById(R.id.courseHolderLL)
        setupCourseViews()



//        recyclerViewCourse = findViewById(R.id.rvCourse)
//        recyclerViewCourse.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        val courseAdapter = CourseAdapter()
//        recyclerViewCourse.adapter = courseAdapter
//        recyclerViewCourse.isNestedScrollingEnabled = false




        recyclerViewInstitute = findViewById(R.id.rvInstitute)
        recyclerViewInstitute.adapter = InstituteAdapter()
        recyclerViewInstitute.layoutManager = GridLayoutManager(this, 5, RecyclerView.VERTICAL, false)



//        recyclerViewGroup = findViewById(R.id.groupRV)
//        recyclerViewGroup.layoutManager = LinearLayoutManager(this)
//        groupAdapter = GroupAdapter(allGroups)
//        recyclerViewGroup.adapter = groupAdapter
//        speciality = findViewById(R.id.specialityTV)
    }

    private fun setupCourseViews(){
        viewsOfCourse.clear()
        linearLayoutCourse.removeAllViews()
        for (i in 0..4) {
            val layout = if (coursesList[i].selected) R.layout.enabled_item else R.layout.disabled_item
            val view = LayoutInflater.from(this).inflate(layout, linearLayoutCourse, false)

            val textView = view.findViewById<TextView>(R.id.tv)
            viewsOfCourse.add(Pair(textView, view))
            textView.text = coursesList[i].name
            linearLayoutCourse.addView(view)

            textView.setOnClickListener { textView ->
                val position = viewsOfCourse.indexOf(viewsOfCourse.find { it.first == textView})
                if (mainViewModel.course.value == null) {
                    selectCourse(position)
                } else {
                    selectCourse(mainViewModel.course.value!!.position)
                    selectCourse(position)
                }
                setupCourseViews()
                mainViewModel.course.value = coursesList[position]
            }


        }
    }

    private fun selectCourse(position: Int){
        coursesList.add(position,
            coursesList[position].copy(selected = !coursesList[position].selected))
        coursesList.removeAt(position + 1)


    }




}