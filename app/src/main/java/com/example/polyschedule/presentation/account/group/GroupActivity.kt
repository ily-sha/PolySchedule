package com.example.polyschedule.presentation.account.group

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.polyschedule.data.network.Api
import com.example.polyschedule.data.network.models.group.GroupContentResponse
import com.example.polyschedule.databinding.ActivityGroupBinding
import com.example.polyschedule.presentation.account.group.createannouncement.CreateAnnouncementActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.SocketTimeoutException

class GroupActivity : AppCompatActivity() {

    private var _binding: ActivityGroupBinding? = null
    private val binding: ActivityGroupBinding
        get() = _binding ?: throw RuntimeException("ActivityGroupBinding is null")

    private var groupId = -1
    private lateinit var groupName: String
    private var invite = ""
        set(value) {
            field = value
            binding.invite.text = field
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        parseIntent()
        binding.invite.setOnClickListener {
            copyInvite()
        }
        binding.imageCopy.setOnClickListener {
            copyInvite()
        }
        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.groupName.text = groupName
        loadGroupInfo()
        binding.refresh.setOnRefreshListener {
            loadGroupInfo()
            binding.refresh.isRefreshing = false
        }
        binding.arrowBack.setOnClickListener {
            finish()
        }


    }

    private fun loadGroupInfo() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = Api.retrofitService.getGroupContent(groupId)
                withContext(Dispatchers.Main){
                    handleGroupContentResponse(response)
                }
            } catch (e: SocketTimeoutException) {
                withContext(Dispatchers.Main){
                    Toast.makeText(this@GroupActivity, "Internet connection problem", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    private fun handleGroupContentResponse(response: Response<GroupContentResponse>) {
        when (response.code()){
            200 -> {
                val body = response.body()
                if (body != null){
                    invite = body.group.invite
                    binding.groupName.text = body.group.name

                }

            }
            400 -> {
                Toast.makeText(this@GroupActivity, "Server error", Toast.LENGTH_SHORT).show()
            }
            404 -> {
                Toast.makeText(this@GroupActivity, "Group not fount", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copyInvite(){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Invite", invite)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, invite, Toast.LENGTH_SHORT).show()
    }

    private fun parseIntent() {
        if (intent.hasExtra(ID_EXTRA) && intent.hasExtra(GROUP_NAME_EXTRA)){
            groupId = intent.getIntExtra(ID_EXTRA, -1)
            groupName = intent.getStringExtra(GROUP_NAME_EXTRA)!!
        } else throw java.lang.RuntimeException("Intent is absent")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun startCreateAnnouncementActivity() {
        startActivity(CreateAnnouncementActivity.newInstance(this))
    }

    companion object {

        private const val ID_EXTRA = "id_extra"
        private const val GROUP_NAME_EXTRA = "group_name_extra"
        fun newIntent(content: Context, id: Int, name: String) = Intent(content, GroupActivity::class.java).apply {
            putExtra(ID_EXTRA, id)
            putExtra(GROUP_NAME_EXTRA, name)
        }
    }
}