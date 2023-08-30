package com.example.polyschedule.presentation.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.registration.RegistrationRequest
import com.example.polyschedule.databinding.ActivitySignUpBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class SignUpActivity : AppCompatActivity() {


    val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            sendCode.setOnClickListener {
                val name = name.text.toString()
                val surname = surname.text.toString()
                val email = email.text.toString()
                var flag = false
                if (!checkUserDataCorrectness(name)) {
                    nameLayout.error = "Некорректное имя"
                    flag = true
                }
                if (!checkUserDataCorrectness(surname)) {
                    surnameLayout.error = "Некорректная фамилия"
                    flag = true
                }
                if (!checkUserDataCorrectness(email) || !email.matches(Regex("""[\w\.\-\_]+@\w+\.\w+"""))) {
                    emailLayout.error = "Некорректная почта"
                    flag = true
                }
                if (!flag) {
                    sendConfirmCodeClicked(
                        name,
                        surname,
                        email
                    )

                }
            }
            name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    nameLayout.error = ""
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            surname.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    surnameLayout.error = ""
                }

                override fun afterTextChanged(p0: Editable?) {}
            })
            email.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    emailLayout.error = ""
                }

                override fun afterTextChanged(p0: Editable?) {}
            })

        }

    }

    private fun checkUserDataCorrectness(data: String): Boolean {
        if (data.isBlank() || MAX_LEN < data.length) return false
        return true
    }

    companion object {
        const val MAX_LEN = 20
    }


    private fun sendConfirmCodeClicked(name: String, surname: String, email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = Api.retrofitService.register(RegistrationRequest(name, surname, email))
                withContext(Dispatchers.Main) {
                    handleResponse(response, email)
                }
            }  catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@SignUpActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                }

            }

        }
    }

    private fun handleResponse(response: Response<String>, email: String){
        when (response.code()) {
            200 -> {
                startActivity(
                    EmailConfirmationActivity.newInstance(
                        this@SignUpActivity,
                        email,
                        EmailConfirmationActivity.Companion.LoginType.REGISTRATION,
                        response.body()!!

                    )
                )
                finish()
            }
            400 -> {
                binding.emailLayout.error = "Аккаунт с такой почтой уже существует"
            }
            else -> {
                binding.emailLayout.error = "Server exception\n Code: ${response.code()} \nBody: ${response.errorBody()}"
            }
        }

    }
}
