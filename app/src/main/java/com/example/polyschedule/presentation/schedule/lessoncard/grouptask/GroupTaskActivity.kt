package com.example.polyschedule.presentation.schedule.lessoncard.grouptask

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.R
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.file.FileDto
import com.example.polyschedule.data.network.models.group.HomeworkResponse
import com.example.polyschedule.data.network.models.homework.CreateHomeworkRequest
import com.example.polyschedule.data.network.models.homework.HomeworkDto
import com.example.polyschedule.data.network.models.schedule.LessonDto
import com.example.polyschedule.databinding.ActivityGroupTaskBinding
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class GroupTaskActivity : AppCompatActivity() {


    private var homework: HomeworkResponse? = null
    private var groupId: Int? = null
    private lateinit var mode: String
    private lateinit var lesson: LessonDto

    private val monthNames = mapOf(
        0 to "января",
        1 to "февраля",
        2 to "марта",
        3 to "апреля",
        4 to "мая",
        5 to "июня",
        6 to "июля",
        7 to "августа",
        8 to "сентября",
        9 to "октября",
        10 to "ноября",
        11 to "декабря"
    )
    private val binding by lazy { ActivityGroupTaskBinding.inflate(layoutInflater) }

    private val selectedFiles = mutableListOf<String>()
    private var deadline = System.currentTimeMillis()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        parseIntent()
        binding.subject.text = lesson.subject
        if (mode == CREATE_MODE) {
            setupCreateMode()
            binding.save.setOnClickListener {
                createHomework()
            }

        }
        if (mode == SHOW_MODE) {
            setupShowMode()
            binding.saveChanges.setOnClickListener {
//                createHomework()
            }
        }

        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.addFile.setOnClickListener {
            selectFiles()
        }

        binding.deadline.setOnClickListener {
            val dataPicker = MaterialDatePicker.Builder.datePicker()
                .setTheme(R.style.ThemeOverlay_App_DatePicker).build()
            dataPicker.show(supportFragmentManager, "dataPicker")
            dataPicker.addOnPositiveButtonClickListener {
                val (startTime, endTime) = lesson.time.split("-")
                val (startHour, startMinute) = startTime.split(":").map { it.toInt() }
                deadline = it + startHour * MILLISECOND_IN_HOUR + startMinute * MILLISECOND_IN_MINUTE
                val simple = SimpleDateFormat("dd-MM")
                val result = Date(it)
                val (day, month) = simple.format(result).split("-").map { it.toInt() }
                binding.deadline.text = "$day ${monthNames[month - 1]}"
            }


        }
    }

    private fun setupShowMode() {
        homework?.let {
            binding.apply {
                deadline.text = it.deadline
                description.setText(it.description)
            }
        }
    }

    private fun setupCreateMode() {
        Calendar.getInstance().apply {
            val month = get(Calendar.MONTH)
            val day = get(Calendar.DAY_OF_MONTH)
            binding.deadline.text = "$day ${monthNames[month]}"
        }
    }


    private fun createHomework() {
        CachedStudentUtils.getToken(applicationContext)?.let { token ->
            groupId?.let {groupId ->
                val description = binding.description.text.toString()
                lifecycleScope.launch(Dispatchers.IO) {
                    Api.retrofitService.createHomework(
                        CreateHomeworkRequest(
                            token, HomeworkDto(
                                groupId = groupId,
                                subject = lesson.subject,
                                description = description,
                                creationTime = System.currentTimeMillis(),
                                editedTime = 0,
                                deadline = deadline,
                                files = getFiles(),
                            )
                        )
                    )
                }
            }
        }
    }

    private fun getFiles(): List<FileDto> {
        val files = mutableListOf<FileDto>()
//        selectedFiles.forEach {
//            files.add(File(it).run {
//                FileDto(name, readBytes())
//            })
//        }
        return files
    }

    private fun parseIntent() {
        if (intent.hasExtra(MODE) && intent.hasExtra(LESSON_EXTRA)) {
            mode = intent.getStringExtra(MODE) ?: throw RuntimeException("EXTRA_MODE is absent")
            lesson = intent.getParcelableExtra(LESSON_EXTRA)
                ?: throw RuntimeException("LESSON_EXTRA is absent")
            if (mode == SHOW_MODE && intent.hasExtra(HOMEWORK_EXTRA)) {
                homework = intent.getParcelableExtra(HOMEWORK_EXTRA)
                    ?: throw RuntimeException("EXTRA_HOMEWORK is absent")
            }
            if (mode == CREATE_MODE && intent.hasExtra(GROUP_ID_EXTRA)){
                groupId = intent.getIntExtra(GROUP_ID_EXTRA, -1)
            }
        } else throw RuntimeException("EXTRA_MODE or EXTRA_LESSON is absent")

    }

    private fun selectFiles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_CONTENT_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CONTENT_GET && resultCode == Activity.RESULT_OK) {
            val path = data?.dataString
            if (path != null) {
                selectedFiles.add(path)
            }

        }
    }

    companion object {
        private const val MILLISECOND_IN_HOUR = 3600000
        private const val MILLISECOND_IN_MINUTE = 60000
        private const val LESSON_EXTRA = "lesson"
        private const val HOMEWORK_EXTRA = "homework"
        private const val GROUP_ID_EXTRA = "group"
        private const val SHOW_MODE = "show"
        private const val MODE = "mode"
        private const val CREATE_MODE = "create"
        fun newInstance(context: Context, lesson: LessonDto, homeworkResponse: HomeworkResponse) =
            Intent(context, GroupTaskActivity::class.java).apply {
                putExtra(MODE, SHOW_MODE)
                putExtra(LESSON_EXTRA, lesson)
                putExtra(HOMEWORK_EXTRA, homeworkResponse)
            }

        fun newInstance(context: Context, lesson: LessonDto, groupId: Int) =
            Intent(context, GroupTaskActivity::class.java).apply {
                putExtra(MODE, CREATE_MODE)
                putExtra(GROUP_ID_EXTRA, groupId)
                putExtra(LESSON_EXTRA, lesson)
            }

        const val REQUEST_CONTENT_GET = 1
    }
}