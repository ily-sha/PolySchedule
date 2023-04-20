package com.example.polyschedule.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.polyschedule.R
import com.example.polyschedule.databinding.InstituteSettingFragmentBinding
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.presentation.adapter.CourseAdapter
import com.example.polyschedule.presentation.adapter.InstituteAdapter
import com.example.polyschedule.presentation.adapter.InstituteAdapter.Companion.rowCount
import java.util.function.Consumer

class InstituteSettingFragment : Fragment() {

    val lifec = "LIFECU"

    private val instituteAdapter by lazy { InstituteAdapter() }
    private val courseAdapter by lazy { CourseAdapter() }

    private val instituteSettingViewModel by lazy {
        ViewModelProvider(this)[InstituteSettingViewModel::class.java]
    }

    private var _binding: InstituteSettingFragmentBinding? = null
    private val binding: InstituteSettingFragmentBinding
        get() = _binding ?: throw RuntimeException("ChooseAttributeFragment binding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Log.d(lifec, "onCreateView")
        return layoutInflater.inflate(R.layout.institute_setting_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Log.d(lifec, "onViewCreated")
        Course.values().iterator().forEachRemaining( Consumer {
            Log.d("LIFECU", "${it.name} - ${it.enable}")
        })
        _binding = InstituteSettingFragmentBinding.bind(view)
        setupRvAdapter()
        observeInstitute()
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Course.values().iterator().forEachRemaining( Consumer {
                    Log.d("MainTr", "${it.name} - ${it.enable}")
                })
                Log.d("MainTr", "Insitute ${event.name}")
            }
        })
    }



    private fun observeInstitute() {
        instituteSettingViewModel.instituteLDfromOut.observe(viewLifecycleOwner) {
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
        findNavController().navigate(InstituteSettingFragmentDirections.actionInstituteSettingFragmentToGroupSetting(course, institute))
    }

    private fun setupCourseAdapter() {
        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.rvCourse.adapter = courseAdapter
        courseAdapter.onCourseItemClicked = {
            Course.values().iterator().forEachRemaining( Consumer {
                Log.d("LIFECU", "onCourseItemClicked ${it.name} - ${it.enable}")
            })
            instituteSettingViewModel.selectedCourse.value = it

            val institute = instituteSettingViewModel.selectedInstitute.value
            if (institute != null) {
                showContinueButton()
            }
        }
    }

    private fun setupInstituteAdapter() {
        binding.rvInstitute.adapter = instituteAdapter
        binding.rvInstitute.layoutManager = StaggeredGridLayoutManager(rowCount, LinearLayoutManager.HORIZONTAL)
        instituteAdapter.onInstituteItemClicked = {
            instituteSettingViewModel.selectedInstitute.value = it
            val course = instituteSettingViewModel.selectedCourse.value
            if (course != null) {
                showContinueButton()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(lifec, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(lifec, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(lifec, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(lifec, "onPause")
    }

    override fun onStop() {
        super.onStop()
        courseAdapter.clear()
        instituteAdapter.clear()
        Course.values().iterator().forEachRemaining( Consumer {
            Log.d("LIFECU", "onStop ${it.name} - ${it.enable}")
        })
        Log.d(lifec, "onStop")
    }

    override fun onDestroy() {
        Log.d(lifec, "onDestroy")

        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(lifec, "onDetach")
        super.onDetach()
    }


    override fun onDestroyView() {
        Log.d(lifec, "onDestroyView")
        super.onDestroyView()
        _binding = null
    }

    private fun showContinueButton() {
        if (binding.continueButton.visibility == View.GONE) {
            binding.continueButton.visibility = View.VISIBLE
        }

    }

    companion object {
        fun newIntent(): InstituteSettingFragment {
            return InstituteSettingFragment()
        }
    }
}