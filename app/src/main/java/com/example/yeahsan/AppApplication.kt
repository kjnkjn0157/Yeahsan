package com.example.yeahsan

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
    }

    //위치서비스
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