package com.example.yeahsan.firebase


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.yeahsan.R
import com.example.yeahsan.ui.MainActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        //토큰이 갱신될 때마다 처리 해주는 작업
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        //FCM 수신 마다 실행
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            Log.e("TAG","message data payload ::: ${message.data}")
        }

        Log.e("TAG"," firebase message from ::: ${message.from}")
        Log.e("TAG","firebase data ::: ${message.data}")
        Log.e("TAG","firebase notification ::: ${message.notification.toString()}")
        Log.e("TAG","firebase ttl ::: ${message.ttl}")

        sendNotification(message.data["title"],message.data["body"])
    }

    fun subscribeTopic(topic : String) {

        //전체 알림 - 사용자 구독 취소 불가능한 토픽
        FirebaseMessaging.getInstance().subscribeToTopic("android_all").addOnCompleteListener {task ->
            if (task.isSuccessful) {
                Log.e("TAG","all 구독 요청 성공 ::: ")
            } else {
                Log.e("TAG","all 구독 요청 실패 ::: ")
            }
        }


        //사용자 구독 취소 가능한 토픽
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                Log.e("TAG","구독 요청 성공 ::: ")
            } else {
                Log.e("TAG","구독 요청 실패 ::: ")
            }
        }
    }

    fun unSubscribeTopic(topic: String) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnCompleteListener {task ->

            if (task.isSuccessful) {
                Log.e("TAG","구독 취소 성공 ::: ")
            } else {
                Log.e("TAG","구독 취소 실패 ::: ")
            }
        }
    }

    private fun sendNotification(messageTitle: String?, messageBody: String?) {

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "위치스의 보부상"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

}