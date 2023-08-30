package com.example.polyschedule.presentation.account.alarmclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class AlarmClockReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG_ALARM, "onReceive $context")
        context?.let {
            if (Settings.canDrawOverlays(context)){
                val intent2 = Intent(it.applicationContext, AlarmClockLockScreenActivity::class.java)
                intent2.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(it, intent2, Bundle())
            }

        }

    }


    companion object {
        val TAG_ALARM = "AlarmClockTAG"

        private const val NOTIFICATION_ID = 515

        fun newIntent(context: Context) = Intent(context, AlarmClockReceiver::class.java)
    }
}