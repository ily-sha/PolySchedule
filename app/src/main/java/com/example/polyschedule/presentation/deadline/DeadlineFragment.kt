package com.example.polyschedule.presentation.deadline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.polyschedule.R
import com.example.polyschedule.databinding.FragmentDeadlineBinding


class DeadlineFragment : Fragment() {

    private var _binding: FragmentDeadlineBinding? = null
    private val binding: FragmentDeadlineBinding
        get() = _binding ?: throw RuntimeException("FragmentDeadlineBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeadlineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}