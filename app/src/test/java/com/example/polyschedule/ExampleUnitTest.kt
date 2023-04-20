package com.example.polyschedule

import android.app.Application
import com.example.polyschedule.data.UniversityImpl
import com.example.polyschedule.domain.usecase.GetGroupsUseCase
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(GetGroupsUseCase(UniversityImpl(Application())))
    }


}