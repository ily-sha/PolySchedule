package com.example.polyschedule.presentation.account.jointogroup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.group.GroupResponse
import com.example.polyschedule.data.network.models.group.JoinToGroupRequest
import com.example.polyschedule.databinding.ActivityJoinToGroupBinding
import com.example.polyschedule.presentation.account.group.GroupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class JoinToGroupActivity : AppCompatActivity() {

    private var _binding: ActivityJoinToGroupBinding? = null
    private val binding: ActivityJoinToGroupBinding
        get() = _binding ?: throw RuntimeException("ActivityJoinToGroupBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityJoinToGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signIn.setOnClickListener {
            val invite = binding.invite.text.toString()
            if (invite.isBlank() || invite.length > binding.inviteLayout.counterMaxLength){
                binding.inviteLayout.error = "Некорректный формат"
            } else {
                it.isEnabled = false
                joinToGroup(invite)
                it.isEnabled = true

            }
        }

        binding.invite.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.inviteLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

    }

    private fun joinToGroup(invite: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val token = CachedStudentUtils.getToken(applicationContext)
            if (token != null) {
                try {
                    val response = Api.retrofitService.joinToGroup(JoinToGroupRequest(token, invite))
                    withContext(Dispatchers.Main){
                        handleResponse(response)
                    }
                }  catch (e: SocketTimeoutException) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@JoinToGroupActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                    }

                }


            } else {
                withContext(Dispatchers.Main){
                    binding.inviteLayout.error = "Token not found"
                }
            }

        }
    }

    private fun handleResponse(response: Response<GroupResponse>) {
        when (response.code()){
            200 -> {
                val body = response.body()
                if (body != null){
                    startActivity(GroupActivity.newIntent(this, body.id, body.name))
                    finish()
                } else {
                    binding.inviteLayout.error = "Server error"
                }
            }
            400 -> {
                binding.inviteLayout.error = "Невозможно присоединиться"
            }
            401 -> {
                binding.inviteLayout.error = "Токен недействительный"
            }
            404 -> {
                binding.inviteLayout.error = "Группа не найдена"
            }
            else -> {
                binding.inviteLayout.error = "Server exception\n Code: ${response.code()} \nBody: ${response.errorBody()}"
            }
        }
    }
}