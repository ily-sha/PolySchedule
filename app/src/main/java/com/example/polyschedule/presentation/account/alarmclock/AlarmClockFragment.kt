package com.example.polyschedule.presentation.account.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.polyschedule.databinding.FragmentAlarmClockBinding
import com.example.polyschedule.presentation.account.alarmclock.AlarmClockReceiver.Companion.TAG_ALARM
import com.example.polyschedule.presentation.MainActivity


class AlarmClockFragment : Fragment() {

    private var _binding: FragmentAlarmClockBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentAlarmClockBinding is null")

    private val viewModel: AlarmClockViewModel by lazy {
        ViewModelProvider(this)[AlarmClockViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmClockBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timePicker.apply {
            setIs24HourView(true)
            hour = 1
            minute = 30
        }
        binding.saveAlarmClock.setOnClickListener {

            if (!Settings.canDrawOverlays(context)) {
                Log.d(TAG_ALARM, "launch")
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                );
                startActivityForResult(intent, 2323)

            } else {
                generateNextAlarmClock()
            }
        }
    }


    private fun generateNextAlarmClock(){
        val timeOfFirstLesson = System.currentTimeMillis()
        val alarmManager = getSystemService(requireContext(), AlarmManager::class.java)
        val alarmManagerInfo =
            AlarmManager.AlarmClockInfo(timeOfFirstLesson + 5000, getAlarmInfoPendingIntent())
//        alarmManager?.setAlarmClock(alarmManagerInfo, getAlarmActionPendingIntent())
    }


    private fun getAlarmInfoPendingIntent(): PendingIntent? {
        val alarmInfoIntent = Intent(requireContext(), MainActivity::class.java)
        alarmInfoIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getActivity(
            requireContext(),
            0,
            alarmInfoIntent,
            PendingIntent.FLAG_MUTABLE
        )
    }

    private fun getAlarmActionPendingIntent(): PendingIntent? {
        val intent = Intent(requireContext(), AlarmClockReceiver::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        return PendingIntent.getBroadcast(requireContext(), 1, intent, PendingIntent.FLAG_MUTABLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}