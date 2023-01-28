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
import kotlin.concurrent.thread
import com.example.polyschedule.presentation.MainViewModel.Companion.COURSE_CHOOSEN
import com.example.polyschedule.presentation.MainViewModel.Companion.INSTITUTE_CHOOSEN
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {


    private lateinit var seleckedCourse: View
    var allGroups = mutableListOf<Speciality>()
    lateinit var recyclerViewGroup: RecyclerView
    lateinit var groupAdapter: GroupAdapter
    lateinit var speciality: TextView
    lateinit var recyclerViewInstitute: RecyclerView
    private lateinit var mainViewModel: MainViewModel

    var courses = mutableListOf(Pair("Курс 1", false), Pair("Курс 2", false), Pair("Курс 3", false),
        Pair("Курс 4", false), Pair("Курс 5", false), Pair("Курс 1", false), Pair("Курс 2", false), Pair("Курс 3", false),
        Pair("Курс 4", false), Pair("Курс 5", false))


    private lateinit var linearLayoutCourse: LinearLayout
    private lateinit var textViewInstitute: TextView
    private lateinit var textViewCourse: TextView
    private lateinit var appbar: AppBarLayout
    private lateinit var toolbar: Toolbar




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViews()

        appbar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            updateViews(abs(verticalOffset))

            }

        }





    private fun updateViews(verticalOffset: Int) {
        val const = 180f
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
        recyclerViewInstitute.layoutManager = GridLayoutManager(this, 5, RecyclerView.HORIZONTAL, false)



//        recyclerViewGroup = findViewById(R.id.groupRV)
//        recyclerViewGroup.layoutManager = LinearLayoutManager(this)
//        groupAdapter = GroupAdapter(allGroups)
//        recyclerViewGroup.adapter = groupAdapter
//        speciality = findViewById(R.id.specialityTV)
//
//



    }
    private val coursesView = mutableListOf<View>()
    private fun setupCourseViews(){
        coursesView.clear()
        for (i in 1..5) {
            val layout = if (courses[i - 1].second) R.layout.enabled_item else R.layout.disabled_item
            val view = LayoutInflater.from(this).inflate(layout, linearLayoutCourse, false)

            val textView = view.findViewById<TextView>(R.id.tv)
            coursesView.add(textView)
            textView.text = courses[i - 1].first
            linearLayoutCourse.addView(view)

            textView.touch { view ->
                val position = coursesView.indexOf(view)
                courses[position] = Pair(courses[position].first, !courses[position].second)
                setupCourseViews()


            }


        }
    }




}