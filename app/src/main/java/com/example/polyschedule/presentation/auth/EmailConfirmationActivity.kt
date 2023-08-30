package com.example.polyschedule.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.R
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.emailconfirmation.EmailConfirmResponse
import com.example.polyschedule.data.network.models.emailconfirmation.EmailConfirmationRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class EmailConfirmationActivity : AppCompatActivity() {

    private val repeatCodeButton by lazy {
        findViewById<CardView>(R.id.repeat_code)
    }
    private val timer by lazy {
        findViewById<TextView>(R.id.timer)
    }
    private val registerButton by lazy {
        findViewById<CardView>(R.id.register)
    }
    private val emailCodeLayout by lazy {
        findViewById<TextInputLayout>(R.id.email_code_layout)
    }

    private lateinit var loginType: LoginType
    private lateinit var temporaryToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_confirmation)
        parseIntent()
        startTimer()
        val emailCode = findViewById<TextInputEditText>(R.id.email_code)

        repeatCodeButton.setOnClickListener {
            it.visibility = View.GONE
            timer.visibility = View.VISIBLE
            startTimer()
        }
        registerButton.setOnClickListener {

            if (emailCode.text.isNullOrBlank()) {
                emailCodeLayout.error = "Неправильный формат"
            } else {
                val code = emailCode.text.toString().toInt()
                it.isEnabled = false
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = Api.retrofitService.emailConfirm(
                            EmailConfirmationRequest(
                                temporaryToken,
                                code
                            )
                        )
                        withContext(Dispatchers.Main) {
                            it.isEnabled = true
                            handleResponse(response)
                        }
                    } catch (e: SocketTimeoutException) {
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@EmailConfirmationActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                        }
                    }


                }
            }

        }
        emailCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailCodeLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }


    private fun handleResponse(response: Response<EmailConfirmResponse>) {

        when (response.code()) {
            200 -> {
                val body = response.body()
                if (body != null) {
                    CachedStudentUtils.apply {
                        setUserInfo(
                            name = body.student.name,
                            surname = body.student.surname,
                            email = body.student.email,
                            token = body.token,
                            studentGroups = body.student.groups,
                            applicationContext = applicationContext
                        )
                    }
                    finish()
                } else {
                    emailCodeLayout.error = "Ошибка сервера"
                }
            }

            400 -> {
                emailCodeLayout.error = "Неправильный код"
            }

            else -> {
                emailCodeLayout.error =
                    "Server exception\n Code: ${response.code()} \nBody: ${response.errorBody()}"
            }
        }
    }


    private fun startTimer() {
        object : CountDownTimer(
            2 * MILLIS_IN_MINUTE,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                timer.text =
                    getString(R.string.next_email_code_after).format(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                repeatCodeButton.visibility = View.VISIBLE
                timer.visibility = View.GONE
            }
        }.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun parseIntent() {
        if (intent.hasExtra(EMAIL_EXTRA) && intent.hasExtra(TEMPORARY_CODE_EXTRA) && intent.hasExtra(
                LOGIN_TYPE_EXTRA
            )
        ) {
            val email = intent.getStringExtra(EMAIL_EXTRA)
            findViewById<TextView>(R.id.email_address).text = "Введите код отправленный на $email"
            loginType = intent.getEnumExtra<LoginType>()!!
            temporaryToken = intent.getStringExtra(TEMPORARY_CODE_EXTRA)!!
        } else throw RuntimeException("Email extra is absent")
    }


    companion object {
        private const val SECONDS_IN_MINUTES = 60
        private const val MILLIS_IN_MINUTE = 60000L
        private const val MILLIS_IN_SECONDS = 1000L
        private const val EMAIL_EXTRA = "email"
        private const val TEMPORARY_CODE_EXTRA = "temporary_code"
        const val LOGIN_TYPE_EXTRA = "login_type"

        fun newInstance(
            context: Context,
            email: String,
            loginType: LoginType,
            temporaryToken: String
        ) = Intent(context, EmailConfirmationActivity::class.java).apply {
            putExtra(EMAIL_EXTRA, email)
            putExtra(loginType)
            putExtra(TEMPORARY_CODE_EXTRA, temporaryToken)
        }

        enum class LoginType {
            ENTRY, REGISTRATION
        }

        inline fun <reified T : Enum<T>> Intent.putExtra(type: T): Intent =
            putExtra(LOGIN_TYPE_EXTRA, type.ordinal)

        inline fun <reified T : Enum<T>> Intent.getEnumExtra(): LoginType? =
            getIntExtra(LOGIN_TYPE_EXTRA, -1)
                .takeUnless { it == -1 }
                ?.let { LoginType::class.java.enumConstants?.get(it) }
    }
}