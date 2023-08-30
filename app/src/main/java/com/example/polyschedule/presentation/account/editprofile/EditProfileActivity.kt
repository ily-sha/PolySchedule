package com.example.polyschedule.presentation.account.editprofile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.example.polyschedule.R
import com.example.polyschedule.data.CachedStudentUtils.removeUserInfo

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        findViewById<CardView>(R.id.exit).setOnClickListener {
            removeUserInfo(applicationContext)
            finish()
        }
    }
}