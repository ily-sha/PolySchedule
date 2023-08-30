package com.example.polyschedule.presentation.settingschedule

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ActivityGroupSettingBinding
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Direction
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.presentation.MainActivity
import com.example.polyschedule.presentation.adapters.GroupSectionAdapter

class GroupSettingActivity : AppCompatActivity() {

    private lateinit var institute: Institute
    private lateinit var course: Course
    private val binding: ActivityGroupSettingBinding by lazy {
        ActivityGroupSettingBinding.inflate(layoutInflater)
    }


    private val groupSettingViewModel by lazy {
        ViewModelProvider(this)[GroupSettingViewModel::class.java]
    }

    private val groupSectionAdapter by lazy {
        GroupSectionAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        groupSettingViewModel.getGroups(course, institute)
        binding.arrowBack.setOnClickListener {
            startActivity(Intent(this, InstituteSettingActivity::class.java))
            finish()
        }
        setupGroupAdapter()
        binding.continueButton.setOnClickListener {
            groupSettingViewModel.selectedGroup.value?.let {
                val direction = Direction(it, institute)
                groupSettingViewModel.setMainDirection(direction)
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }
        }
    }

    private fun parseIntent() {
        if (intent.hasExtra(INSTITUTE_EXTRA) && intent.hasExtra(COURSE_EXTRA)){
            institute = intent.getSerializableExtra(INSTITUTE_EXTRA) as Institute
            course = intent.getSerializableExtra(COURSE_EXTRA) as Course
        } else throw RuntimeException("extra is absent")
    }

    private fun setupGroupAdapter() {
        binding.rvGroupSection.adapter = groupSectionAdapter
        groupSectionAdapter.onGroupClicked = {
            groupSettingViewModel.selectedGroup.value = it
            if (binding.continueButton.visibility == View.GONE) {
                binding.continueButton.visibility = View.VISIBLE
            }
        }
        binding.rvGroupSection.recycledViewPool.setMaxRecycledViews(
            GroupSectionAdapter.MAIN_VIEWTYPE,
            30
        )

        groupSettingViewModel.groupLD.observe(this) {
            binding.progressBar.visibility = View.GONE
            groupSectionAdapter.sections = it
        }
    }



    companion object {
        private const val COURSE_EXTRA = "course"
        private const val INSTITUTE_EXTRA = "institute"
        fun newInstance(context: Context, institute: Institute, course: Course) =
            Intent(context, GroupSettingActivity::class.java).apply {
                putExtra(INSTITUTE_EXTRA, institute)
                putExtra(COURSE_EXTRA, course)
            }
    }
}