package com.example.yeahsan.service.beacon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.util.Log
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.BeaconMapVO
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.service.`interface`.ContentResult
import com.rd.utils.CoordinatesUtils
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

    private val scannedBeaconList: HashSet<BeaconMapVO> = hashSetOf()
    private var findBeaconCheckMap: HashMap<String, String> = hashMapOf()
    /*refactoring*/
    private var doorList : ArrayList<DoorListVO>? = arrayListOf()
    private var doorBeaconList : HashMap<BeaconMapVO, Int> = hashMapOf()
    private var doorClearList : ArrayList<DoorListVO>? = arrayListOf()

    //    private var indoorList: ArrayList<DoorListVO>? = null
//    private var outdoorList: ArrayList<DoorListVO>? = null
//    private val indoorBeaconList: HashMap<BeaconMapVO, Int> = hashMapOf()
//    private val outdoorBeaconList: HashMap<BeaconMapVO, Int> = hashMapOf()
//    private var indoorClearList : ArrayList<DoorListVO>? = arrayListOf()
//    private var outdoorClearList : ArrayList<DoorListVO>? = arrayListOf()

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: BeaconUtil? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        private lateinit var application: AppApplication

        fun getInstance(_context: Context, appApplication: AppApplication): BeaconUtil {
            return instance ?: synchronized(this) {
                instance ?: BeaconUtil().also {
                    context = _context
                    instance = it
                    application = appApplication
                }
            }
        }
    }

    private lateinit var beaconManager: BeaconManager

    override fun onBeaconServiceConnect() {

        Log.e("TAG","onBeaconServiceConnect :::")

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
                if (it.isNotEmpty() && doorBeaconList.size > 0) {
                    var findBeacon: Beacon? = null
                    var comparison: Int? = null

                    for (beacon: Beacon in beacons) {
                        //      Log.e("TAG", "내가 찾은 비콘 ::: major ${beacon.id2} :::: minor ${beacon.id3}")
                        val key = BeaconMapVO(
                            Integer.parseInt(beacon.id2.toString()),
                            Integer.parseInt(beacon.id3.toString())
                        )

                        if (doorBeaconList.containsKey(key)) {
                            doorBeaconList[key]?.let { rssi ->
                                if (beacon.rssi > rssi) {
                                    if (comparison != null) {
                                        if (comparison!! < rssi) {
                                            comparison = rssi
                                            findBeacon = beacon
                                        }
                                    } else {
                                        comparison = rssi
                                        findBeacon = beacon
                                    }
                                }
                            }
//                            indoorBeaconList[key]?.let { rssi ->
//                                if (beacon.rssi > rssi) {
//                                    if (comparison != null) {
//                                        if (comparison!! < rssi) {
//                                            comparison = rssi
//                                            findBeacon = beacon
//                                        }
//                                    } else {
//                                        comparison = rssi
//                                        findBeacon = beacon
//                                    }
//                                }
//                            }
                        }
                    }

                    findBeacon?.let { Beacon ->
                        scannedBeaconList.add(
                            BeaconMapVO(
                                Integer.parseInt(Beacon.id2.toString()),
                                Integer.parseInt(Beacon.id3.toString())
                            )
                        )
                        checkBeaconData(scannedBeaconList, application.contentResult)
                    }
                }
            }
        }
    }



    private fun checkBeaconData(list: HashSet<BeaconMapVO>, result: ContentResult) {

        findBeaconCheckMap = AppDataManager.getInstance(application).getCheckBeaconFindItem()

        doorList?.let {
            for (i in 0 until it.size) {
                for (j in 0 until it[i].beaconList.size) {
                    // 사용자가 찾아낸 비콘 컨텐츠 한번이라도 보지 않았을 경우에만 허용
                    if (!findBeaconCheckMap.containsKey(it[i].code)) {
                        val temp = BeaconMapVO(Integer.parseInt(it[i].beaconList[j].major), Integer.parseInt(it[i].beaconList[j].minor))
                        if (list.contains(temp)) {
                            AppDataManager.getInstance(application).setCheckBeaconFindItem(it[i].code)
                            doorClearList?.let { clearList ->
                                if (!clearList.contains(it[i])) {
                                    result.onContentReceived(it[i])
                                }
                            }
                        }
                    }
                }
            }
        }

//        outdoorList?.let {
//            for (i in 0 until it.size) {
//                for (j in 0 until it[i].beaconList.size) {
//                    // 사용자가 찾아낸 비콘 컨텐츠 한번이라도 보지 않았을 경우에만 허용
//                    if (!findBeaconCheckMap.containsKey(it[i].code)) {
//                        val temp = BeaconMapVO(
//                            Integer.parseInt(it[i].beaconList[j].major),
//                            Integer.parseInt(it[i].beaconList[j].minor)
//                        )
//                        if (list.contains(temp)) {
//                            AppDataManager.getInstance(application).setCheckBeaconFindItem(it[i].code)
//                            outdoorClearList?.let { clearList ->
//                                if (!clearList.contains(it[i])) {
//                                    result.onContentReceived(it[i])
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }


    private fun getData() {

        AppDataManager.getInstance(application).getBaseData {
            it?.body?.indoorList?.forEach { item -> doorList?.add(item) }
            it?.body?.outdoorList?.forEach { item -> doorList?.add(item) }
        }

        doorList?.forEach {list ->
                    val beaconMap = BeaconMapVO(Integer.parseInt(list.beaconList[0].major), Integer.parseInt(list.beaconList[0].minor))
                doorBeaconList[beaconMap] = list.beaconList[0].aRssi
        }

        AppDataManager.getInstance(application).getOutdoorMissionClearItems()?.let { it.forEach { doorClearList?.add(it) } }
        AppDataManager.getInstance(application).getIndoorMissionClearItems()?.let { it.forEach { doorClearList?.add(it) } }

        //        AppDataManager.getInstance(application).getBaseData {
//
//            indoorList = it?.body?.indoorList
//            outdoorList = it?.body?.outdoorList
//
//            it?.body?.indoorList?.forEach { list ->
//                val beaconMap = BeaconMapVO(
//                    Integer.parseInt(list.beaconList[0].major),
//                    Integer.parseInt(list.beaconList[0].minor)
//                )
//                indoorBeaconList[beaconMap] = list.beaconList[0].aRssi
//            }
//            it?.body?.outdoorList?.forEach { list ->
//                val beaconMap = BeaconMapVO(
//                    Integer.parseInt(list.beaconList[0].major),
//                    Integer.parseInt(list.beaconList[0].minor)
//                )
//                outdoorBeaconList[beaconMap] = list.beaconList[0].aRssi
//            }
//        }
//
//        outdoorClearList = AppDataManager.getInstance(application).getOutdoorMissionClearItems()
//        indoorClearList = AppDataManager.getInstance(application).getIndoorMissionClearItems()

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