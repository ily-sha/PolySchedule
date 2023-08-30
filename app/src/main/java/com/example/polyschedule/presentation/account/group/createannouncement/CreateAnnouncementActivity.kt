package com.example.polyschedule.presentation.account.group.createannouncement

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.polyschedule.R
import com.example.polyschedule.databinding.ActivityCreateAnnouncementBinding
import com.example.polyschedule.databinding.ActivityCreateGroupBinding

class CreateAnnouncementActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCreateAnnouncementBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, CreateAnnouncementActivity::class.java)
    }
}