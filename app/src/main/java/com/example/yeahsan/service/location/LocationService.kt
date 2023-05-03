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
import com.example.yeahsan.service.ContentEvent
import com.google.android.gms.location.*

class LocationService : Service() {

    val SERVICE_STRING = "location Service"

    private var indoorLocationList : ArrayList<DoorListVO> = arrayListOf()
    private var outdoorLocationList : ArrayList<DoorListVO> = arrayListOf()

    override fun onCreate() {
        super.onCreate()

        getData()
    }

    private fun getData() {

        AppDataManager(application as AppApplication).getSampleData {
            it?.let {
                for(i in 0 until it.body.indoorList.size) {
                    indoorLocationList.add(it.body.indoorList[i])
                }

                for(i in 0 until it.body.outdoorList.size) {
                    outdoorLocationList.add(it.body.outdoorList[i])
                }
            }
        }


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

    private var mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            if (locationResult.lastLocation != null) {

                val myLatitude = locationResult.lastLocation!!.latitude
                val myLongitude = locationResult.lastLocation!!.longitude
                Log.e("TAG", "my location update ::: $myLatitude ::: $myLongitude")

                //내부 컨텐츠
                if (indoorLocationList.size > 0 ) {
                    for (i in 0 until indoorLocationList.size) {
                        for (j in 0 until indoorLocationList[i].locationList.size) {
                            val contentLat = indoorLocationList[i].locationList[j].latitude.toDouble()
                            val contentLon = indoorLocationList[i].locationList[j].longitude.toDouble()
                            val radius = indoorLocationList[i].locationList[j].range
                            val distance = getDistance(myLatitude,myLongitude,contentLat,contentLon,"meter")

                            if (distance < radius) {
                                // 컨텐츠가 잡혔을 경우 여기서 처리
                                ContentEvent.getInstance(applicationContext).setContent(indoorLocationList[i])
                            }
                        }
                    }
                }

                //외부컨텐츠
                if (outdoorLocationList.size > 0 ) {
                    for (i in 0 until outdoorLocationList.size) {
                        for (j in 0 until outdoorLocationList[i].locationList.size) {
                            val contentLat = outdoorLocationList[i].locationList[j].latitude.toDouble()
                            val contentLon = outdoorLocationList[i].locationList[j].longitude.toDouble()
                            val radius = outdoorLocationList[i].locationList[j].range
                            val distance = getDistance(myLatitude,myLongitude,contentLat,contentLon,"meter")

                            if (distance < radius) {
                                // 컨텐츠가 잡혔을 경우 여기서 처리
                                ContentEvent.getInstance(applicationContext).setContent(outdoorLocationList[i])
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }

    private fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double, unit: String): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        if (unit == "kilometer") {
            dist *= 1.609344
        } else if (unit == "meter") {
            dist *= 1609.344
        }
        return dist
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