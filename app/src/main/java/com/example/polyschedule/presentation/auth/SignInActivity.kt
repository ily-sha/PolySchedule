package com.example.polyschedule.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.R
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.login.LoginRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class SignInActivity : AppCompatActivity() {

    private val emailLayout by lazy {
        findViewById<TextInputLayout>(R.id.email_layout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val emailView = findViewById<TextInputEditText>(R.id.email)
        findViewById<CardView>(R.id.send_code).setOnClickListener {
            val email = emailView.text.toString()
            if (email.matches(Regex("""[\w\.\-\_]+@\w+\.\w+"""))) {
                sendConfirmCode(email)
            } else {
                emailLayout.error = "Некорректная почта"
            }

        }
        emailView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                emailLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun sendConfirmCode(email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = Api.retrofitService.login(LoginRequest(email))
                withContext(Dispatchers.Main) {
                    handleResponse(response, email)
                }
            }  catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@SignInActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                }

            }



        }
    }

    private fun handleResponse(response: Response<String>, email: String){
        when (response.code()) {
            200 -> {
                startActivity(
                    EmailConfirmationActivity.newInstance(
                        this@SignInActivity,
                        email,
                        EmailConfirmationActivity.Companion.LoginType.ENTRY,
                        response.body()!!
                    )
                )
                finish()
            }
            401 -> {
                emailLayout.error = "Аккаунт не найден"
            }
            else -> {
                emailLayout.error = "Server exception\n Code: ${response.code()} \nBody: ${response.errorBody()}"
            }
        }
    }
}