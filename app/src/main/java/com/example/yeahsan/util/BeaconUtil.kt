package com.example.yeahsan.util

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.BeaconListVO
import com.example.yeahsan.data.api.model.BeaconMapVO
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.ContentEvent
import com.example.yeahsan.service.beacon.BeaconConstant
import com.example.yeahsan.service.beacon.BeaconScanResult
import com.witches.utils.lsh.Hash
import org.altbeacon.beacon.*

/**
 * 1. 위치정보 서비스
 *  - 결과값 처리
 * 2. 비콘 서비스 : 2종류 ( iBeacon , Eddystone)
 *  - 결과값 처리
 * 3. 수신되는 결과값을 처리하는 클래스 => 유니티 액티비티 호출 ( 중요 )
 *  - 데이터 ( Q1 , Q2, Q3 .. Qn ) => Q1 처리 => Q1 제거
 *  => Set
 *  - 순차 처리 , 병렬 처리
 */

class BeaconUtil() : InternalBeaconConsumer {
    //main 에서 권한 검사 먼저 하고 있다면 서비스 가동
    private var indoorList : ArrayList<DoorListVO>? = null
    private var outdoorList : ArrayList<DoorListVO>? = null

    private val indoorBeaconList: HashMap<BeaconMapVO, Int> = hashMapOf()
    private val outdoorBeaconList: HashMap<BeaconMapVO, Int> = hashMapOf()
    private val scannedBeaconList: HashSet<BeaconMapVO> = hashSetOf()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: BeaconUtil? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getInstance(_context: Context): BeaconUtil {
            return instance ?: synchronized(this) {
                instance ?: BeaconUtil().also {
                    context = _context
                    instance = it
                }
            }
        }
    }

    private lateinit var beaconManager: BeaconManager

    override fun onBeaconServiceConnect() {

        getData()

        beaconManager = BeaconManager.getInstanceForApplication(context)
//        beaconManager.setEnableScheduledScanJobs(true) // 이 코드를 설정해야 블루투스 스캔을 중지하지않음
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconConstant.BEACON_PARSE))
        beaconManager.bindInternal(this)

        beaconManager.removeAllRangeNotifiers()

        //비콘 모니터링
//        beaconManager.addMonitorNotifier(object : MonitorNotifier {
//            override fun didEnterRegion(region: Region?) {
//                Log.e("TAG", "::: 최소 하나의 비콘 발견 ::: ")
//            }
//
//            override fun didExitRegion(region: Region?) {
//                Log.e("TAG", "::: 더이상 비콘을 찾을 수 없음:::")
//            }
//
//            override fun didDetermineStateForRegion(state: Int, region: Region?) {
//                if (state == 0) {
//                    Log.e("TAG", "비콘이 보이지 않는 상태 ::: state ::: $state")
//                } else {
//                    Log.e("TAG", "비콘이 보이는 상태 ::: state :::$state")
//                }
//            }
//
//        })
//
//        try {
//            beaconManager.startMonitoring(
//                Region(
//                    BeaconConstant.I_BEACON_UUID,
//                    null,
//                    null,
//                    null
//                )
//            )
//        } catch (ignored: RemoteException) {
//
//        }

        //비콘 range 값 확인
        val region = Region(BeaconConstant.I_BEACON_UUID, null, null, null)

        beaconManager.startRangingBeacons(region)
        beaconManager.addRangeNotifier { beacons, region ->

            beacons?.let {
                if (it.isNotEmpty() && indoorBeaconList.size > 0 && outdoorBeaconList.size > 0) {
                    for (beacon: Beacon in beacons) {
                        Log.e("TAG", "내가 찾은 비콘 ::: major ${beacon.id2} :::: minor ${beacon.id3}")
                        val key = BeaconMapVO(Integer.parseInt(beacon.id2.toString()), Integer.parseInt(beacon.id3.toString()))
                        if (indoorBeaconList.containsKey(key) || outdoorBeaconList.containsKey(key)) {
                            indoorBeaconList[key]?.let {
                                if (beacon.rssi > it) {
                                    //1분에 한번씩 허용
                                    val recentTime = beacon.firstCycleDetectionTimestamp
                                    val currentTime = System.currentTimeMillis()
                                    val delay = currentTime - recentTime
                                    if (delay > BeaconConstant.DURATION_BEACON_SCAN) {
                                        scannedBeaconList.add(BeaconMapVO(Integer.parseInt(beacon.id2.toString()),Integer.parseInt(beacon.id3.toString())))
                                        checkBeaconData(scannedBeaconList,object : BeaconScanResult{
                                            override fun onBeaconScanned(item : DoorListVO) {
                                                //다른 class(위치,비콘 result 처리할 곳 ) 로 보내서 처리
                                                ContentEvent.getInstance(applicationContext).setContent(item)
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkBeaconData(list : HashSet<BeaconMapVO> , result : BeaconScanResult) {

        indoorList?.let {
            for (i in 0 until it.size) {
                for (j in 0 until it[i].beaconList.size) {
                    val temp = BeaconMapVO(it[i].beaconList[j].major,it[i].beaconList[j].minor)
                    if (list.contains(temp)) {
                       result.onBeaconScanned(it[i])
                    }
                }
            }
        }

        outdoorList?.let {
            for (i in 0 until it.size) {
                for (j in 0 until it[i].beaconList.size) {
                    val temp = BeaconMapVO(it[i].beaconList[j].major,it[i].beaconList[j].minor)
                    if (list.contains(temp)) {
                        result.onBeaconScanned(it[i])
                    }
                }
            }
        }
    }


    private fun getData() {
        // major,minor 가 키 , value 엔 어떤 content rssi 값보다 dbm 이 클 경우 팝업 띄우기 .....
        AppDataManager(applicationContext as AppApplication).getSampleData {

            indoorList = it?.body?.indoorList
            outdoorList = it?.body?.outdoorList

            it?.body?.indoorList?.forEach { list ->
                val beaconMap = BeaconMapVO(list.beaconList[0].major, list.beaconList[0].minor)
                indoorBeaconList[beaconMap] = list.beaconList[0].aRssi
            }
            it?.body?.outdoorList?.forEach { list ->
                val beaconMap = BeaconMapVO(list.beaconList[0].major, list.beaconList[0].minor)
                outdoorBeaconList[beaconMap] = list.beaconList[0].aRssi
            }
        }
    }


    override fun getApplicationContext(): Context {
        return context.applicationContext
    }

    override fun unbindService(connection: ServiceConnection?) {
        beaconManager.unbindInternal(this)
    }

    override fun bindService(intent: Intent?, connection: ServiceConnection?, mode: Int): Boolean {
        return false
    }


}