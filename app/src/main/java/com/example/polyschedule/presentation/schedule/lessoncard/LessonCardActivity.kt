package com.example.polyschedule.presentation.schedule.lessoncard

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.emailconfirmation.Student
import com.example.polyschedule.data.network.models.schedule.LessonDto
import com.example.polyschedule.databinding.ActivityLessonCardBinding
import com.example.polyschedule.presentation.schedule.lessoncard.grouptask.GroupDialogFragment
import com.example.polyschedule.presentation.schedule.lessoncard.grouptask.GroupTaskActivity
import com.example.polyschedule.presentation.schedule.lessoncard.grouptask.HomeworkDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException

class LessonCardActivity : AppCompatActivity() {

    private lateinit var lesson: LessonDto


    private val binding by lazy { ActivityLessonCardBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        if (lesson.homework.isEmpty()) {
            binding.createTask.visibility = View.VISIBLE
            binding.openTask.visibility = View.GONE
        } else {
            binding.createTask.visibility = View.VISIBLE
            binding.openTask.visibility = View.VISIBLE
        }
        binding.subject.text = lesson.subject
        binding.auditory.text = lesson.auditory
        if (lesson.teachers == null) {
            binding.teacherLayout.visibility = View.GONE
        } else {
            binding.teacherLayout.visibility = View.VISIBLE
            binding.teacher.text = lesson.teachers
        }

        binding.openTask.setOnClickListener {
            if (lesson.homework.size == 1) {
                startActivity(
                    GroupTaskActivity.newInstance(
                        this@LessonCardActivity,
                        lesson,
                        lesson.homework[0]
                    )
                )
            } else {
                HomeworkDialogFragment(lesson).show(
                    supportFragmentManager,
                    null
                )
            }


        }
        binding.createTask.setOnClickListener {
            uploadStudentsGroup()
            CachedStudentUtils.getStudentInfo(applicationContext)?.studentGroups?.let {
                if (it.size == 1) {
                    startActivity(
                        GroupTaskActivity.newInstance(
                            this@LessonCardActivity,
                            lesson,
                            it.keys.singleOrNull()!!
                        )
                    )
                } else {
                    GroupDialogFragment(lesson, it).show(supportFragmentManager, null)
                }
            }


        }
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

//    private fun isTaskForOneGroup() = lesson.associateBy { it.groupId }.size == 1

    private fun uploadStudentsGroup() {
        val token = CachedStudentUtils.getToken(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            token?.let {
                try {
                    val response = Api.retrofitService.getStudentInfo(it)
                    handleStudentInfoResponse(response, token, applicationContext)
                } catch (e: SocketTimeoutException) {

                }


            }

        }
    }

    private fun handleStudentInfoResponse(
        response: Response<Student>,
        token: String,
        applicationContext: Context
    ) {
        when (response.code()) {
            200 -> {
                response.body()?.let {
                    CachedStudentUtils.setUserInfo(
                        token = token,
                        name = it.name,
                        surname = it.surname,
                        email = it.email,
                        studentGroups = it.groups,
                        applicationContext
                    )
                }
            }
        }

    }

    private fun parseIntent() {
        if (intent.hasExtra(LESSON_EXTRA)) {
            lesson = intent.getParcelableExtra(LESSON_EXTRA)!!
        } else throw RuntimeException("Lesson extra is absent")

    }


    companion object {
        private const val LESSON_EXTRA = "lesson"
        fun newInstance(lessonDto: LessonDto) = Bundle().apply {
            putParcelable(LESSON_EXTRA, lessonDto)
        }
    }
}