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
import org.altbeacon.beacon.*

class BeaconUtil() : InternalBeaconConsumer {
    //main 에서 권한 검사 먼저 하고 있다면 서비스 가동
    private val indoorBeaconList: HashMap<BeaconMapVO, Int> = hashMapOf()
    private val outdoorBeaconList : HashMap<BeaconMapVO,Int> = hashMapOf()
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
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.bindInternal(this)

        beaconManager.removeAllRangeNotifiers()

        //비콘 모니터링
        beaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didEnterRegion(region: Region?) {
                Log.e("TAG", "::: 최소 하나의 비콘 발견 ::: ")
            }

            override fun didExitRegion(region: Region?) {
                Log.e("TAG", "::: 더이상 비콘을 찾을 수 없음:::")
            }

            override fun didDetermineStateForRegion(state: Int, region: Region?) {
                if (state == 0) {
                    Log.e("TAG", "비콘이 보이지 않는 상태 ::: state ::: $state")
                } else {
                    Log.e("TAG", "비콘이 보이는 상태 ::: state :::$state")
                }
            }

        })

        try {
            beaconManager.startMonitoring(
                Region(
                    "2ACA4240-13EE-11E4-9416-0002A5D5C51B",
                    null,
                    null,
                    null
                )
            )
        } catch (ignored: RemoteException) {

        }


        //비콘 range 값 확인
        val region = Region("2ACA4240-13EE-11E4-9416-0002A5D5C51B", null, null, null)

        beaconManager.startRangingBeacons(region)
        beaconManager.addRangeNotifier { beacons, region ->
            beacons?.let {
                if (it.isNotEmpty()) {
                    if (indoorBeaconList.size > 0 && outdoorBeaconList.size > 0) {
                        for (beacon: Beacon in beacons) {

                            Log.e("TAG", "내가 찾은 비콘 ::: major $beacon.id2 :::: minor $beacon.id3")
                        }
                    }
                }
            }
            Log.e("TAG", "region ::: ${beacons.size} ::: ${region.uniqueId}")
        }
    }

    private fun getData() {
        // major,minor 가 키 , value 엔 어떤 content rssi 값보다 dbm 이 클 경우 팝업 띄우기 .....
        AppDataManager(applicationContext as AppApplication).getSampleData {
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