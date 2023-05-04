package com.example.yeahsan.service.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.yeahsan.AppApplication
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.data.api.model.LocationListVO

import com.google.android.gms.location.*

class LocationService : Service() {

    val SERVICE_STRING = "location Service"

    private lateinit var mLocationCallback : LocationCallback

    override fun onCreate() {
        super.onCreate()

        LocationUtil.getInstance(applicationContext,application as AppApplication).getData()

        mLocationCallback = LocationUtil.getInstance(this,(application as AppApplication)).mLocationCallback
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            val action = intent.action.toString()
            if (action == "startLocation") {
                startLocationService()
            } else if (action == "stopLocation") {
                stopLocationService()
            }
        }
        return super.onStartCommand(intent, flags, startId)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val nc = NotificationChannel(
            SERVICE_STRING,
            "Location Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val nm = getSystemService(NotificationManager::class.java)

        nm.createNotificationChannel(nc)
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
            .setContentTitle("myNotify__Location")
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentText("my location service running ::: ")
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
    }


    private fun startLocationService() {
        val notification = createNotification()
        val mLocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 4000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(4000)
            .setMaxUpdateDelayMillis(1000)
            .build()

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper())
        startForeground(2, notification)
    }

    private fun stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        stopSelf();
    }


    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}