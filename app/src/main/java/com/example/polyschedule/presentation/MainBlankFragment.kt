package com.example.polyschedule.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.repoimpl.DirectionRepositoryImpl
import com.example.polyschedule.domain.usecase.direction.GetDirectionUseCase
import com.example.polyschedule.presentation.SettingSchedule.GroupSettingFragment

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
                val universityEntity = GetDirectionUseCase(DirectionRepositoryImpl(requireActivity().application)).getDirection(it.toInt())
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