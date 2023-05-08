package com.example.yeahsan.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.`interface`.ContentResult
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationUtil {

    private var indoorLocationList : ArrayList<DoorListVO> = arrayListOf()
    private var outdoorLocationList : ArrayList<DoorListVO> = arrayListOf()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: LocationUtil? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private lateinit var application : AppApplication

        fun getInstance(_context: Context,appApplication: AppApplication): LocationUtil {
            return instance ?: synchronized(this) {
                instance ?: LocationUtil().also {
                    context = _context
                    instance = it
                    application = appApplication
                }
            }
        }
    }

    fun getData() {

        AppDataManager.getInstance(application).getSampleData {
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

    var mLocationCallback = object : LocationCallback() {
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
                                application.contentResult.onContentReceived(outdoorLocationList[i])
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
                                application.contentResult.onContentReceived(outdoorLocationList[i])
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
}