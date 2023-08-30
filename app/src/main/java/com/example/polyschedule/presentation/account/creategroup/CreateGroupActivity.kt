package com.example.polyschedule.presentation.account.creategroup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.data.CachedStudentUtils
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.group.CreateGroupRequest
import com.example.polyschedule.data.network.models.group.GroupDto
import com.example.polyschedule.data.network.models.group.GroupResponse
import com.example.polyschedule.databinding.ActivityCreateGroupBinding
import com.example.polyschedule.presentation.account.group.GroupActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class CreateGroupActivity : AppCompatActivity() {




    private var _binding: ActivityCreateGroupBinding? = null
    private val binding: ActivityCreateGroupBinding
        get() = _binding ?: throw RuntimeException("ActivityCreateGroupBinding is null")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCreateGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createCardview.setOnClickListener {
            val groupName = binding.groupName.text.toString()
            if (isGroupNameCorrect(groupName)){
                it.isEnabled = false
                createGroup(groupName)
                it.isEnabled = true
            }
        }
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.groupName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.groupNameLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun isGroupNameCorrect(groupName: String): Boolean {
        if (groupName.isBlank() || groupName.length > binding.groupNameLayout.counterMaxLength) {
            binding.groupNameLayout.error = "Некорректный формат"
            return false
        }
        if (groupName.contains(Regex("""[:;]"""))){
            binding.groupNameLayout.error = "Название группы не должно содержать знаки : и ;"
            return false
        }
        return true
    }

    private fun createGroup(groupName: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val token = CachedStudentUtils.getToken(applicationContext)
            if (token != null) {
                try {
                    val response = Api.retrofitService.createGroup(CreateGroupRequest(token, GroupDto(groupName)))
                    withContext(Dispatchers.Main){
                        handleResponse(response)
                    }
                }  catch (e: SocketTimeoutException) {
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@CreateGroupActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                    }

                }


            } else {
                withContext(Dispatchers.Main){
                    binding.groupNameLayout.error = "Token not found"
                }
            }

        }
    }

    private fun handleResponse(response: Response<GroupResponse>) {
        when (response.code()) {
            200 -> {
                val body = response.body()
                if (body != null) {
                    startActivity(GroupActivity.newIntent(this, body.id, body.name))
                    finish()
                } else {
                    binding.groupNameLayout.error = "Server error"
                }
            }
            400 -> {
                binding.groupNameLayout.error = "Невозможно создать группу"
            }
            401 -> {
                binding.groupNameLayout.error = "Токен недействительный"
            }
            else -> {
                binding.groupNameLayout.error = "Server exception\n Code: ${response.code()} \nBody: ${response.errorBody()}"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



    companion object {

//        @JvmStatic
//        fun newInstance() ()

    }
}