package com.example.yeahsan.service.location

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

class LocationUtil {

    /*refactoring*/
    private var doorLocationList: ArrayList<DoorListVO> = arrayListOf()
    private var clearDoorList: ArrayList<DoorListVO>? = arrayListOf()

//    private var indoorLocationList: ArrayList<DoorListVO> = arrayListOf()
//    private var outdoorLocationList: ArrayList<DoorListVO> = arrayListOf()
//    private var clearIndoorList: ArrayList<DoorListVO> = arrayListOf()
//    private var clearOutdoorList: ArrayList<DoorListVO> = arrayListOf()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: LocationUtil? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private lateinit var application: AppApplication

        fun getInstance(_context: Context, appApplication: AppApplication): LocationUtil {
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

        AppDataManager.getInstance(application).getBaseData {
            it?.let {
                it.body.outdoorList.forEach{ doorLocationList.add(it) }
                it.body.indoorList.forEach { doorLocationList.add(it) }
            }
        }

        clearDoorList = AppDataManager.getInstance(application).getIndoorMissionClearItems()
        AppDataManager.getInstance(application).getOutdoorMissionClearItems()?.let {
            it.forEach {
                clearDoorList?.add(it)
            }
        }

//        AppDataManager.getInstance(application).getBaseData {
//            it?.let {
//                for (i in 0 until it.body.indoorList.size) {
//                    indoorLocationList.add(it.body.indoorList[i])
//                }
//
//                for (i in 0 until it.body.outdoorList.size) {
//                    outdoorLocationList.add(it.body.outdoorList[i])
//                }
//            }
//        }
//
//        AppDataManager.getInstance(application).getIndoorMissionClearItems()?.let {
//            clearIndoorList = it
//        }
//        AppDataManager.getInstance(application).getOutdoorMissionClearItems()?.let {
//            indoorLocationList = it
//        }
    }

    var mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            if (locationResult.lastLocation != null) {
                val myLatitude = locationResult.lastLocation!!.latitude
                val myLongitude = locationResult.lastLocation!!.longitude
//                Log.e("TAG", "my location update ::: $myLatitude ::: $myLongitude")

                //내부 컨텐츠
                if (doorLocationList.size > 0) {
                    for (i in 0 until doorLocationList.size) {
                        for (j in 0 until doorLocationList[i].locationList.size) {
                            val contentLat =
                                doorLocationList[i].locationList[j].latitude.toDouble()
                            val contentLon =
                                doorLocationList[i].locationList[j].longitude.toDouble()
                            val radius = doorLocationList[i].locationList[j].range
                            val distance = getDistance(
                                myLatitude,
                                myLongitude,
                                contentLat,
                                contentLon,
                                "meter"
                            )
                            if (distance < radius) { // 범위 안에 사용자가 있는지
                                if (!clearDoorList?.contains(doorLocationList[i])!!) { // 미션 클리어된 아이템이 아니라면
                                    application.contentResult.onContentReceived(doorLocationList[i])
                                }
                            }
                        }
                    }
                }

                //외부컨텐츠
//                if (outdoorLocationList.size > 0) {
//                    for (i in 0 until outdoorLocationList.size) {
//                        for (j in 0 until outdoorLocationList[i].locationList.size) {
//                            val contentLat =
//                                outdoorLocationList[i].locationList[j].latitude.toDouble()
//                            val contentLon =
//                                outdoorLocationList[i].locationList[j].longitude.toDouble()
//                            val radius = outdoorLocationList[i].locationList[j].range
//                            val distance = getDistance(
//                                myLatitude,
//                                myLongitude,
//                                contentLat,
//                                contentLon,
//                                "meter"
//                            )
//                            if (distance < radius) {
//                                if (!clearOutdoorList.contains(outdoorLocationList[i])) {
//                                    application.contentResult.onContentReceived(outdoorLocationList[i])
//                                }
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180 / Math.PI
    }

    private fun getDistance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        unit: String
    ): Double {
        val theta = lon1 - lon2
        var dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(
                deg2rad(lat2)
            ) * Math.cos(deg2rad(theta))
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