package com.example.polyschedule

import android.app.Application
import androidx.lifecycle.Observer
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetGroupsUseCase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val application = androidx.test.core.app.ApplicationProvider.getApplicationContext<Application>()

        val liveData = GetGroupsUseCase(UniversityImpl(application)).getGroups(1, 95)
//        liveData.observe(Observer { println(it) })
    }
}