package com.example.polyschedule.presentation.settingschedule

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ActivityInstituteSettingBinding
import com.example.polyschedule.presentation.adapters.CourseAdapter
import com.example.polyschedule.presentation.adapters.InstituteAdapter

class InstituteSettingActivity : AppCompatActivity() {

    private val instituteAdapter by lazy { InstituteAdapter() }
    private val courseAdapter by lazy { CourseAdapter() }

    private val instituteSettingViewModel by lazy {
        ViewModelProvider(this)[InstituteSettingViewModel::class.java]
    }

    private var _binding: ActivityInstituteSettingBinding? = null
    private val binding: ActivityInstituteSettingBinding
        get() = _binding ?: throw RuntimeException("ActivityInstituteSettingBinding binding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityInstituteSettingBinding.inflate(layoutInflater)
        setupRvAdapter()
        observeInstitute()
        instituteSettingViewModel.loadInstitute()
        setContentView(binding.root)

    }

    private fun observeInstitute() {
        instituteSettingViewModel.instituteLD.observe(this) {
            binding.progressBar.visibility = View.GONE
            instituteAdapter.instituteList = it
        }
    }

    private fun setupRvAdapter() {
        binding.continueButton.setOnClickListener {
            continueButtonClicked()
        }
        setupCourseAdapter()
        setupInstituteAdapter()
    }

    private fun continueButtonClicked() {
        val institute = instituteSettingViewModel.selectedInstitute.value!!
        val course = instituteSettingViewModel.selectedCourse.value!!
        startActivity(GroupSettingActivity.newInstance(this, institute, course))
        finish()
    }

    private fun setupCourseAdapter() {
        binding.rvCourse.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvCourse.adapter = courseAdapter
        courseAdapter.onCourseItemClicked = {
            instituteSettingViewModel.selectedCourse.value = it

            val institute = instituteSettingViewModel.selectedInstitute.value
            if (institute != null) {
                showContinueButton()
            }
        }
    }

    private fun setupInstituteAdapter() {
        binding.rvInstitute.adapter = instituteAdapter
        binding.rvInstitute.layoutManager = StaggeredGridLayoutManager(InstituteAdapter.rowCount, LinearLayoutManager.HORIZONTAL)
        instituteAdapter.onInstituteItemClicked = {
            instituteSettingViewModel.selectedInstitute.value = it
            val course = instituteSettingViewModel.selectedCourse.value
            if (course != null) {
                showContinueButton()
            }
        }
    }

    private fun showContinueButton() {
        if (binding.continueButton.visibility == View.GONE) {
            binding.continueButton.visibility = View.VISIBLE
        }
    }


    override fun onStop() {
        super.onStop()
        courseAdapter.clear()
        instituteAdapter.clear()
    }
}