package com.example.yeahsan

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.service.`interface`.ContentResult
import com.example.yeahsan.service.location.LocationConstant
import com.example.yeahsan.service.location.LocationService
import com.example.yeahsan.ui.popup.GameZonePopupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val intent = Intent(applicationContext, LocationService::class.java)
        intent.action = LocationConstant.LOCATION_SERVICE_STOP
        stopService(intent)
        Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
    }


    val contentResult = object : ContentResult {

        override fun onContentReceived(content: DoorListVO) {
            Log.e("TAG","content ::: $content")
            if(AppDataManager.getInstance(this@AppApplication).isGamePopupResult()) {

            } else {
                if(AppDataManager.getInstance(this@AppApplication).getArrivePopupUse()) {
                    val scannedContents: ArrayList<DoorListVO> = arrayListOf()
                    scannedContents.add(content)
                    if (scannedContents.size > 0) {
                        val intent = Intent(this@AppApplication,GameZonePopupActivity::class.java)
                        intent.putExtra(AppConstants.EXTRA_ITEM,scannedContents[0])
                        startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
                    }
                }
            }
        }
    }


     fun checkAllClear(type : String) {
        Log.e("TAG","checkAllClear :: ")
        var checkList : ArrayList<DoorListVO>? = null
        var originSize : Int? = null
        var checkListSize : Int? = null
        val temp = AppDataManager.getInstance(this).getBaseData()

        if (type == AppConstants.IN_DOOR_TYPE) {
            checkList =  AppDataManager.getInstance(this).getIndoorMissionClearItems()
            originSize = temp?.body?.indoorList?.size?.minus(1)
            checkListSize = checkList?.size?.plus(1)
            Log.e("TAG","origin size ::: $originSize")
            Log.e("TAG","checkList size ::: ${checkListSize}")
            if (checkListSize == originSize) {
                AppDataManager.getInstance(this).setMissionAllClearIndoor(true)

            } else {
                AppDataManager.getInstance(this).setMissionAllClearIndoor(false)
            }

        } else {
            checkList = AppDataManager.getInstance(this).getOutdoorMissionClearItems()
            originSize = temp?.body?.outdoorList?.size?.minus(1)
            checkListSize = checkList?.size?.plus(1)
            Log.e("TAG","origin size ::: $originSize")
            Log.e("TAG","checkList size ::: ${checkListSize}")
            if (checkListSize == originSize) {
                Log.e("TAG","application all clear out :: all")
                AppDataManager.getInstance(this).setMissionAllClearOutdoor(true)

            } else {
                Log.e("TAG","application all clear out :: all false ")
                AppDataManager.getInstance(this).setMissionAllClearOutdoor(false)
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



