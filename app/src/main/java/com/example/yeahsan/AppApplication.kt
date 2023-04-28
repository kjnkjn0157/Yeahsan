package com.example.yeahsan

import android.Manifest
import android.app.ActivityManager
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.yeahsan.service.BeaconService
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel
import com.google.android.gms.maps.model.LatLng

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    /**
     * Beacon Service */
    fun startBeaconService(isBeaconServiceRunning : Boolean) {
        if (!isBeaconServiceRunning) {
            val intent = Intent(applicationContext, BeaconService::class.java)
            intent.action = "startBeacon"
            startService(intent)
            Toast.makeText(this.applicationContext, "Beacon service started", Toast.LENGTH_SHORT).show()
        }
    }

    fun stopBeaconService(isBeaconServiceRunning : Boolean) {
        if (isBeaconServiceRunning) {
            val intent = Intent(applicationContext, BeaconService::class.java)
            intent.action = "stopBeacon"
            startService(intent)
            Toast.makeText(this, "Beacon service stopped", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * location update*/
    fun getLocation(): LatLng {

        var latLng = LatLng(0.0,0.0)
        val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var location = if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "서비스가 제한될 수 있습니다.", Toast.LENGTH_SHORT).show()

        } else {
            val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            val lat = location?.latitude
            val long = location?.longitude

            lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1f,
                gpsLocationListener
            )

            lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000L,
                1f,
                gpsLocationListener
            )

            lat?.let {
                long?.let {
                    latLng = LatLng(lat, long)
                }
            }

        }
        return latLng
    }

    private val gpsLocationListener: LocationListener = LocationListener {

        var lon = it.longitude
        var lat = it.latitude
    }

}