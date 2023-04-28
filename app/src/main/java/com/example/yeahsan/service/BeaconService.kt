package com.example.yeahsan.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.yeahsan.R
import com.example.yeahsan.util.BeaconUtil

class BeaconService : Service() {

    val SERVICE_STRING = "beaconService"

    override fun onCreate() {
        super.onCreate()
        Log.e("TAG", "beacon servicr ::: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e("TAG", "onStartCommand   ::: ")

        if (intent != null) {
            Log.e("TAG", "startBeaconService intent not null  ::: ")
            val action = intent.action.toString()
            if (action == "startBeacon") {
                startBeaconService()
            } else if (action == "stopBeacon") {
                stopBeaconService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun startBeaconService() {

        Log.e("TAG", "startBeaconService  ::: ")
        val notification = createNotification()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("TAG", "permission null ::: ")
            return
        }
        BeaconUtil.getInstance(this).onBeaconServiceConnect()
        Log.e("TAG", "permission not null ::: ")
        startForeground(2, notification)
    }

    private fun stopBeaconService() {
        Log.e("TAG", "stopService ::: ")
        stopForeground(true);
        stopSelf();
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        val resultIntent = Intent()
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            resultIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat
            .Builder(this, SERVICE_STRING)
            .setContentTitle("myNotify__beacon")
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentText("my beacon service running ::: ")
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val nc = NotificationChannel(
            SERVICE_STRING,
            "Beacon Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val nm = getSystemService(NotificationManager::class.java)

        nm.createNotificationChannel(nc)
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}