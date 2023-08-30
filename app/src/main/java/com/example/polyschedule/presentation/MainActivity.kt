package com.example.polyschedule.presentation


import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.polyschedule.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.repoimpl.DirectionRepositoryImpl
import com.example.polyschedule.domain.entity.Institute
import com.example.polyschedule.domain.usecase.direction.GetDirectionUseCase
import com.example.polyschedule.presentation.schedule.ScheduleFragment.Companion.DIRECTION_EXTRA
import com.example.polyschedule.presentation.settingschedule.InstituteSettingActivity


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        askNotificationPermission()
    }


    override fun onStart() {
        super.onStart()
        checkCache()
    }


    fun checkCache() {
        if (CacheUtils.hasKey(CacheUtils.DIRECTION, applicationContext)) {
            CacheUtils.getString(CacheUtils.DIRECTION, applicationContext)?.let {
                val universityEntity = GetDirectionUseCase(DirectionRepositoryImpl(application)).getDirection(it.toInt())

                bottomNavigationView = findViewById(R.id.bottomNavigationView)
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_fragment_container) as NavHostFragment?
                navHostFragment?.let {
                    bottomNavigationView.setupWithNavController(it.navController)
                    it.navController.navigate(R.id.scheduleFragment, Bundle().apply {
                        putSerializable(DIRECTION_EXTRA, universityEntity)
                    })
                }

            }
        } else {
            startActivity(Intent(this, InstituteSettingActivity::class.java))
            finish()
        }
    }



}