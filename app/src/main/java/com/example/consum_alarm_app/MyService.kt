package com.example.consum_alarm_app

import android.app.Service
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyService : FirebaseMessagingService() {
    override fun onNewToken(p0: String) {

        super.onNewToken(p0)

        Log.d("MyLog","fcm token....$p0")

    }



    override fun onMessageReceived(p0: RemoteMessage) {

        super.onMessageReceived(p0)

        Log.d("MyLog", "fcm message...... ${p0.notification}")

        Log.d("MyLog", "fcm message...... ${p0.data}")



    }

}