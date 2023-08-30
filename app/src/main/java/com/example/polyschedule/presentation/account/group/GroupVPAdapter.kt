package com.example.polyschedule.presentation.account.group

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GroupVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return when (position){
            0 -> GroupHomeworkFragment()
            else-> AnnouncementHomeworkFragment()
        }

    }
}