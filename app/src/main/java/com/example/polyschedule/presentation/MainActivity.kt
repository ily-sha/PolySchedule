package com.example.polyschedule.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.domain.usecase.GetUniversityUseCase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) checkCache()
    }

    private fun checkCache() {
        supportFragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.main_fragment_container, ChooseAttributeFragment.newIntent())
                    .commit()
//        CacheUtils.instance!!.apply {
//            if ((hasKey(
//                    CacheUtils.COURSE_KEY, this@MainActivity
//                )) && hasKey(
//                    CacheUtils.INSTITUTE_KEY, this@MainActivity
//                ) && hasKey(CacheUtils.GROUP_KEY, this@MainActivity)
//            ) {
//                supportFragmentManager.beginTransaction().addToBackStack(null).replace(
//                    R.id.main_fragment_container, ScheduleFragment.newIntent(
//                        this.getString(CacheUtils.COURSE_KEY, this@MainActivity)!!,
//                        this.getString(CacheUtils.INSTITUTE_KEY, this@MainActivity)!!,
//                        this.getString(CacheUtils.GROUP_KEY, this@MainActivity)!!
//                    )
//                ).commit()
//            } else {
//                supportFragmentManager.beginTransaction().addToBackStack(null)
//                    .replace(R.id.main_fragment_container, ChooseAttributeFragment.newIntent())
//                    .commit()
//            }
//        }

    }

}