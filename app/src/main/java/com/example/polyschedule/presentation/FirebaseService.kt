package com.example.polyschedule.presentation

import com.example.polyschedule.data.CacheUtils
import com.example.polyschedule.data.CachedStudentUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.net.URL


class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        println(token)
        CacheUtils.setString(CacheUtils.REGISTRATION_TOKEN, token, applicationContext)
        if (CachedStudentUtils.getStudentInfo(applicationContext) != null){
            URL("http://95.55.18.142?token=${token}")

        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println(message)
    }


    
}

