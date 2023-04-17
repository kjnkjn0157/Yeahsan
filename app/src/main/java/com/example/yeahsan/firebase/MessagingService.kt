package com.example.yeahsan.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        //토큰이 갱신될 때마다 처리 해주는 작업
        Log.e("TAG", " onNewToken ::: $token")
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        //FCM 수신 마다 실행
        super.onMessageReceived(message)

        //todo firebase data 받아서 나중에 처리
        Log.e("TAG"," firebase message from ::: ${message.from}")
        Log.e("TAG","firebase data ::: ${message.data}")
        Log.e("TAG","firebase notification ::: ${message.notification.toString()}")
        Log.e("TAG","firebase ttl ::: ${message.ttl}")

    }

}