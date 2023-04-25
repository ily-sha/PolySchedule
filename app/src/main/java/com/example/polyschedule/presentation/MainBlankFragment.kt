package com.example.polyschedule.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetUniversityUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView


/**
 * A simple [Fragment] subclass.
 * Use the [MainBlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainBlankFragment : Fragment() {

    private lateinit var showBottomNav: GroupSettingFragment.ShowBottomNav
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GroupSettingFragment.ShowBottomNav) {
            showBottomNav = context
        } else throw RuntimeException("Activity which use MainBlankFragment must implements GroupSettingFragment.ShowBottomNav")
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (savedInstanceState == null) {
            checkCache()
        }
    }

    private fun checkCache() {
        if (CacheUtils.instance?.hasKey(CacheUtils.MAIN_GROUP, requireContext().applicationContext) == true) {
            CacheUtils.instance?.getString(CacheUtils.MAIN_GROUP, requireContext().applicationContext)?.let {
                val universityEntity = GetUniversityUseCase(UniversityImpl(requireActivity().application)).getUniversity(it.toInt())
                findNavController().navigate(
                    MainBlankFragmentDirections.actionMainBlankFragmentToScheduleFragment(
                        universityEntity
                    )
                )
                showBottomNav.showBottomNavigationView()

            }
        } else {

            findNavController().navigate(MainBlankFragmentDirections.actionMainBlankFragmentToInstituteSettingFragment())
        }
    }








}