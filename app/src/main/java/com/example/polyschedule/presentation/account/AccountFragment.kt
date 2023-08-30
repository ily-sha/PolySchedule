package com.example.polyschedule.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.polyschedule.R
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.emailconfirmation.Student
import com.example.polyschedule.databinding.FragmentAccountBinding
import com.example.polyschedule.presentation.account.group.GroupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding: FragmentAccountBinding
        get() = _binding ?: throw RuntimeException("FragmentAccountBinding is null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserInfo()
        binding.setAlarmClock.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionAccountFragmentToAlarmClockFragment())
        }
        binding.createGroup.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_createGroupActivity)
        }
        binding.joinToGroup.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_joinToGroupActivity)
        }
        binding.signIn.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_signInActivity)
        }
        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_signUpActivity)
        }
        binding.editProfile.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_editProfileActivity)
        }
        binding.refreshView.setOnRefreshListener {
            updateUserInfo()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )

    }

    private fun updateUserInfo() {
        val token = CachedStudentUtils.getToken(requireContext().applicationContext)
        if (token != null) {
            lifecycleScope.launch(Dispatchers.Main) {
                launch(Dispatchers.IO) {
                    try {
                        val response = Api.retrofitService.getStudentInfo(token)
                        withContext(Dispatchers.Main) {
                            handleUpdateResponse(response, token)
                        }
                    } catch (e: SocketTimeoutException) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Internet connection problem",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.join()
                binding.refreshView.isRefreshing = false
            }
        }
    }

    private fun handleUpdateResponse(response: Response<Student>, token: String) {
        when (response.code()) {
            200 -> {
                val student = response.body()
                if (student != null) {
                    CachedStudentUtils.setUserInfo(
                        name = student.name,
                        surname = student.surname,
                        email = student.email,
                        token = token,
                        studentGroups = student.groups,
                        applicationContext = requireContext().applicationContext
                    )
                    setupAccountView()
                }
            }
            401 -> {
                CachedStudentUtils.removeUserInfo(requireContext().applicationContext)
                Toast.makeText(requireContext(), "Student is unauthorized", Toast.LENGTH_SHORT).show()
                setupAccountView()
            } else ->  {
                setupAccountView()
            }

        }
    }


    override fun onStart() {
        super.onStart()
        setupAccountView()

    }


    private fun setupAccountView() {
        val student = CachedStudentUtils.getStudentInfo(requireContext().applicationContext)
        if (student == null) {
            binding.profile.text = this@AccountFragment.getString(R.string.my_profile)
            binding.loginLayout.visibility = View.VISIBLE
            binding.accountInfoLayout.visibility = View.GONE
            binding.groupsContainer.removeAllViews()
        } else {
            binding.loginLayout.visibility = View.GONE
            binding.accountInfoLayout.visibility = View.VISIBLE
            binding.profile.text = student.name
            if (student.studentGroups.isNotEmpty()) {
                binding.groupsContainer.removeAllViews()
                binding.groups.visibility = View.VISIBLE
                for (group in student.studentGroups) {
                    val cardView = layoutInflater.inflate(
                        R.layout.light_green_button,
                        binding.groupsContainer,
                        false
                    )
                    cardView.findViewById<TextView>(R.id.inner_tv).text = group.value
                    cardView.setOnClickListener {
                        startActivity(
                            GroupActivity.newIntent(
                                requireContext(),
                                group.key,
                                group.value
                            )
                        )
                    }
                    binding.groupsContainer.addView(cardView)
                }
            }

        }
    }

    override fun onStop() {
        super.onStop()
        binding.groupsContainer.removeAllViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}