package com.example.polyschedule.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetUniversityUseCase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) checkCache()
    }

    private fun checkCache() {
        if (CacheUtils.instance?.hasKey(CacheUtils.MAIN_GROUP, applicationContext) == true) {
            CacheUtils.instance?.getString(CacheUtils.MAIN_GROUP, applicationContext)?.let {
                val university =
                    GetUniversityUseCase(UniversityImpl(application)).getUniversity(it.toInt())
                supportFragmentManager.beginTransaction().addToBackStack(null).replace(
                    R.id.main_fragment_container, ScheduleFragment.newIntent(
                        university
                    )
                ).commit()
            }
        } else {
            supportFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.main_fragment_container, ChooseAttributeFragment.newIntent())
                .commit()
        }
    }

}