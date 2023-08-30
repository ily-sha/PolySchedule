package com.example.polyschedule.presentation.account.group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.polyschedule.R
import com.example.polyschedule.databinding.FragmentGroupBinding

class GroupContentFragment : Fragment() {


    private val binding by lazy {
        FragmentGroupBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = GroupVPAdapter(this)
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        viewPager.adapter = adapter

        binding.apply {
            disabledHomework.setOnClickListener {
                showHomework()
                viewPager.currentItem = 0
            }
            disabledAnnouncement.setOnClickListener {
                showAnnouncement()
                viewPager.currentItem = 1
            }
            addAnnouncement.setOnClickListener {
                (requireActivity() as GroupActivity).startCreateAnnouncementActivity()
            }
        }

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        showHomework()
                    }
                    else -> {
                        showAnnouncement()
                    }
                }
            }
        })
    }

    private fun showAnnouncement() {
        binding.disabledHomework.visibility = View.VISIBLE
        binding.enabledAnnouncement.visibility = View.VISIBLE
        binding.addAnnouncement.visibility = View.VISIBLE

        binding.enabledHomework.visibility = View.GONE
        binding.disabledAnnouncement.visibility = View.GONE

    }

    private fun showHomework(){
        binding.enabledHomework.visibility = View.VISIBLE
        binding.disabledAnnouncement.visibility = View.VISIBLE

        binding.disabledHomework.visibility = View.GONE
        binding.enabledAnnouncement.visibility = View.GONE
        binding.addAnnouncement.visibility = View.GONE


    }

}
