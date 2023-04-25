package com.example.polyschedule.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.polyschedule.databinding.FragmentGroupSettingBinding
import com.example.polyschedule.domain.entity.UniversityEntity
import com.example.polyschedule.presentation.adapter.GroupSectionAdapter
import com.example.polyschedule.presentation.adapter.GroupSectionAdapter.Companion.MAIN_VIEWTYPE


class GroupSettingFragment : Fragment() {

    private var _binding: FragmentGroupSettingBinding? = null
    private val binding: FragmentGroupSettingBinding
    get() = _binding ?: throw IllegalStateException("FragmentGroupSettingBinding is null")

    interface ShowBottomNav {
        fun showBottomNavigationView()
    }

    private lateinit var showBottomNav: GroupSettingFragment.ShowBottomNav
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ShowBottomNav) {
            showBottomNav = context
        } else throw RuntimeException("Activity which use MainBlankFragment must implements GroupSettingFragment.ShowBottomNav")
        groupSettingViewModel.getGroups(args.course, args.institute)
    }


    private val groupSettingViewModel by lazy {
        ViewModelProvider(requireActivity())[GroupSettingViewModel::class.java]
    }

    private val groupSectionAdapter by lazy {
        GroupSectionAdapter()
    }



    private val args by lazy {
        navArgs<GroupSettingFragmentArgs>().value
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.arrowBack.setOnClickListener {
            findNavController().popBackStack()
        }
        setupGroupAdapter()
        binding.continueButton.setOnClickListener {
            groupSettingViewModel.selectedGroup.value?.let {
                val universityEntity = UniversityEntity(it, args.institute)
                groupSettingViewModel.setMainSchedule(universityEntity)
                findNavController().navigate(GroupSettingFragmentDirections.actionGroupSettingFragmentToScheduleFragment(universityEntity))
                showBottomNav.showBottomNavigationView()


            }
        }

    }

    private fun setupGroupAdapter() {
        binding.rvGroupSection.adapter = groupSectionAdapter
        groupSectionAdapter.onGroupClicked = {
            groupSettingViewModel.selectedGroup.value = it
            if (binding.continueButton.visibility == View.GONE) {
                binding.continueButton.visibility = View.VISIBLE
            }
        }
        binding.rvGroupSection.recycledViewPool.setMaxRecycledViews(MAIN_VIEWTYPE, 30)

        groupSettingViewModel.groupLD.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            groupSectionAdapter.sections = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}