package com.example.yeahsan

import android.app.Application
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.service.`interface`.ContentResult
import com.example.yeahsan.service.location.LocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis


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

    val singleContext = newSingleThreadContext("ContentContext")
    var contents: ArrayList<DoorListVO> = arrayListOf()


    val contentResult = object : ContentResult {
        override fun onContentReceived(content: DoorListVO) {
            //  단일 결과
            Log.e("TAG","content data ::: $content")
            //  -> Content Event
        }

        override fun onContentsReceived(contents: ArrayList<DoorListVO>) {
            //  다중 결과
            Log.e("TAG","content result arr ::: $contents")
            //  -> Content Event
        }
    }

    fun visibleState(view: View) :Int {
        val state = if (view.visibility == View.GONE) {
             View.VISIBLE
        } else {
            View.GONE
        }
       return state
    }

}