package com.example.yeahsan

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.service.location.LocationService
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
     * location service */

    fun startLocationService() {

        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = "startLocation"
        startService(intent)
        Toast.makeText(this.applicationContext, "Location service started", Toast.LENGTH_SHORT).show()
    }

    fun stopLocationService() {
        val intent = Intent(applicationContext, BeaconService::class.java)
        intent.action = "stopLocation"
        startService(intent)
        Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
    }


}