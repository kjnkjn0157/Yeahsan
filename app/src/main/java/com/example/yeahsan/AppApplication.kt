package com.example.yeahsan

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.service.`interface`.ContentResult
import com.example.yeahsan.service.location.LocationConstant
import com.example.yeahsan.service.location.LocationService
import com.example.yeahsan.ui.popup.GameZonePopupActivity
import java.util.Collections.list


/**
 *                              AppApplication
 *                                     |
 *        |-------------------------------------------------------|
 *    BeaconUtil <--> BeaconService                         LocationUtil <--> LocationService
 *        |                                                       |
 *        ----------------------- ContentResult --------------------
 *
 *
 * BeaconService : 비콘 초기화 및 해제
 * BeaconUtil : 비콘 검증 및 결과 처리
 * BeaconConstant : 비콘 서비스 관련 상수
 * LocationService : 위치정보 초기화 및 해제
 * LocationUtil : 위치정보 검증 및 결과 처리
 * LocationConstant : 위치정보 서비스 관련 상수
 *
 * ContentResult : 컨텐츠 결과 전달 인터페이스
 *
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }

    override fun onTerminate() {
        super.onTerminate()
    }

    /**
     * Beacon Service */
    fun startBeaconService(isBeaconServiceRunning: Boolean) {
        if (!isBeaconServiceRunning) {
            val intent = Intent(applicationContext, BeaconService::class.java)
            intent.action = "startBeacon"
            startService(intent)
            Toast.makeText(this.applicationContext, "Beacon service started", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun stopBeaconService(isBeaconServiceRunning: Boolean) {
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
        intent.action = LocationConstant.LOCATION_SERVICE_START
        startService(intent)
        Toast.makeText(this.applicationContext, "Location service started", Toast.LENGTH_SHORT)
            .show()
    }

    fun stopLocationService() {
        val intent = Intent(applicationContext, BeaconService::class.java)
        intent.action = LocationConstant.LOCATION_SERVICE_STOP
        startService(intent)
        Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
    }


    val contentResult = object : ContentResult {

        override fun onContentReceived(content: DoorListVO) {
            Log.e("TAG","content ::: $content")
            if(AppDataManager.getInstance(this@AppApplication).isGamePopupResult()) {
                val scannedContents: ArrayList<DoorListVO> = arrayListOf()
                scannedContents.add(content)
                if (scannedContents.size > 0) {
                    val intent = Intent(this@AppApplication,GameZonePopupActivity::class.java)
                    intent.putExtra("item",scannedContents[0])
                    startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
                }
            } else {

            }
        }
    }



    fun visibleState(view: View): Int {
        val state = if (view.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.GONE
        }
        return state
    }

}



