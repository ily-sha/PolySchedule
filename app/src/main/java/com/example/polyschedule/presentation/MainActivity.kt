package com.example.polyschedule.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.polyschedule.R
import com.example.polyschedule.data.CacheUtils


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkCache()
    }

    private fun checkCache() {
        CacheUtils.instance!!.apply {
            if ((hasKey(
                    CacheUtils.COURSE_KEY,
                    this@MainActivity
                )) && hasKey(
                    CacheUtils.INSTITUTE_KEY,
                    this@MainActivity
                ) && hasKey(CacheUtils.GROUP_KEY, this@MainActivity)
            ) {
                supportFragmentManager.beginTransaction().addToBackStack(null).add(
                        R.id.main_fragment_container, ScheduleFragment.newIntent(
                            this.getString(CacheUtils.COURSE_KEY, this@MainActivity)!!,
                            this.getString(CacheUtils.INSTITUTE_KEY, this@MainActivity)!!,
                            this.getString(CacheUtils.GROUP_KEY, this@MainActivity)!!
                        )
                    ).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, ChooseAttributeFragment.newIntent()).commit()
            }
        }
    }

}