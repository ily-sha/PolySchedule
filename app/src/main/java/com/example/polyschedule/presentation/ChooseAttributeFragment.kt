package com.example.polyschedule.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.databinding.ChooseAttributeFragmentBinding
import com.example.polyschedule.domain.entity.Course
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.presentation.adapter.CourseAdapter
import com.example.polyschedule.presentation.adapter.GroupAdapter
import com.example.polyschedule.presentation.adapter.InstituteAdapter
import kotlin.math.abs

class ChooseAttributeFragment : Fragment() {

    private var courseContainerHeight = 0

    private val instituteAdapter by lazy {
        InstituteAdapter()
    }
    private val groupAdapter by lazy {
        GroupAdapter()
    }

    private val chooseAttributeViewModel by lazy {
        ViewModelProvider(this)[ChooseAttributeViewModel::class.java]
    }

    private var _binding: ChooseAttributeFragmentBinding? = null
    private val binding: ChooseAttributeFragmentBinding
        get() = _binding ?: throw RuntimeException("ChooseAttributeFragment binding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.choose_attribute_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChooseAttributeFragmentBinding.bind(view)

        setupRvAdapter()

        binding.appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            moveViews(abs(verticalOffset.toFloat()))
        }
        observeCourse()
        observeInstitute()
        obverseGroup()
        binding.courseContainer.post {
            courseContainerHeight = binding.courseContainer.height
        }
    }


    private fun obverseGroup() {
        chooseAttributeViewModel.groupLD.observe(viewLifecycleOwner) {
            if (binding.continueButton.visibility == View.GONE) binding.continueButton.visibility =
                View.VISIBLE
        }
    }

    private fun observeInstitute() {
        chooseAttributeViewModel.instituteLDfromOut.observe(viewLifecycleOwner) {
            instituteAdapter.instituteList = it
        }
        chooseAttributeViewModel.instituteLD.observe(viewLifecycleOwner) {
            val course = chooseAttributeViewModel.courseLD.value
            binding.chosenTvInsitute.text = it.getAbbr()
            if (course != null) {
                uploadGroups(course, it)
            }
        }
    }

    private fun observeCourse() {
        chooseAttributeViewModel.courseLD.observe(viewLifecycleOwner) {
            val institute = chooseAttributeViewModel.instituteLD.value
            binding.chosenTvCourse.text = it.name
            if (institute != null) {
                uploadGroups(it, institute)
            }
        }
    }


    private fun moveViews(verticalOffset: Float) {
        val tvInstituteCollapsePosition =
            resources.getDimension(R.dimen.vertical_margin) + courseContainerHeight
        val whenRVInstituteCollapse = tvInstituteCollapsePosition + courseContainerHeight
        binding.instituteContainer.apply {
            if (verticalOffset < tvInstituteCollapsePosition) {
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
            if (verticalOffset < whenRVInstituteCollapse) this.translationY =
                (verticalOffset / binding.appBarLayout.totalScrollRange) * -3000f
            this.rotationX = 90 * (verticalOffset / binding.appBarLayout.totalScrollRange) * 10f
            this.visibility = if (rotationX >= 90) View.INVISIBLE else View.VISIBLE
        }
        binding.chosenCourseContainer.alpha = verticalOffset / binding.appBarLayout.totalScrollRange
        binding.chosenInstituteContainer.alpha =
            verticalOffset / binding.appBarLayout.totalScrollRange
    }

    private fun setupRvAdapter() {
        binding.continueButton.setOnClickListener {
            continueButtonClicked()
        }
        setupCourseAdapter()
        setupInstituteAdapter()
        setupGroupAdapter()
    }

    private fun setupGroupAdapter() {
        binding.rvGroup.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvGroup.adapter = groupAdapter
        groupAdapter.onGroupItemClicked = {
            chooseAttributeViewModel.groupLD.value = it
        }
    }

    private fun continueButtonClicked() {
        val instituteId = chooseAttributeViewModel.instituteLD.value!!.getId().toString()
        val groupId = chooseAttributeViewModel.groupLD.value!!.id.toString()
        val course = (chooseAttributeViewModel.courseLD.value!!.position + 1).toString()
        CacheUtils.instance!!.apply {
            setString(
                CacheUtils.GROUP_KEY, groupId, requireContext()
            )
            setString(
                CacheUtils.INSTITUTE_KEY, instituteId, requireContext()
            )
            setString(
                CacheUtils.COURSE_KEY, course, requireContext()
            )
        }
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment_container, ScheduleFragment.newIntent(course, instituteId, groupId)
        ).commit()
    }

    private fun setupCourseAdapter() {
        binding.rvCourse.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        val courseAdapter = CourseAdapter()
        binding.rvCourse.adapter = courseAdapter
        courseAdapter.onCourseItemClicked = {
            chooseAttributeViewModel.courseLD.value = it
        }
    }

    private fun setupInstituteAdapter() {
        binding.rvInstitute.layoutManager =
            GridLayoutManager(requireContext(), 4, RecyclerView.HORIZONTAL, false)
        binding.rvInstitute.adapter = instituteAdapter
        instituteAdapter.onInstituteItemClicked = {
            chooseAttributeViewModel.instituteLD.value = it
        }
    }

    private fun uploadGroups(course: Course, institute: Institute) {
        if (binding.textViewGroup.visibility == View.GONE) {
            binding.textViewGroup.visibility = View.VISIBLE
        }
        chooseAttributeViewModel.getGroups(course.position + 1, institute.getId())
        chooseAttributeViewModel.groupLDfromOut.observe(viewLifecycleOwner) {
            groupAdapter.groupList = it
        }
    }

    companion object {
        fun newIntent(): ChooseAttributeFragment {
            return ChooseAttributeFragment()
        }
    }
}