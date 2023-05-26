package com.example.polyschedule.presentation.Account.AlarmClock

import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.polyschedule.R


class AlarmClockLockScreenActivity : AppCompatActivity() {

    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        showWhenLockedAndTurnScreenOn()
        Log.d(AlarmClockReceiver.TAG_ALARM, "onCreate")

        super.onCreate(savedInstanceState)

//        binding = ActivityLockscreenBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_alarm)
        findViewById<TextView>(R.id.alarm).setOnClickListener {
            Log.d(AlarmClockReceiver.TAG_ALARM, "finish")
            if (ringtone != null && ringtone!!.isPlaying) {
                ringtone!!.stop()
                finish()
            }
        }
        var notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this, notificationUri)
        if (ringtone == null) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(this, notificationUri)
        }
        ringtone?.play()
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)

        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        Log.d(AlarmClockReceiver.TAG_ALARM, "onStop")

        if (ringtone != null && ringtone!!.isPlaying) {
            ringtone!!.stop()
        }
        super.onStop()
    }

}