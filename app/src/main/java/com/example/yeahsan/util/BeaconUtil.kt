package com.example.yeahsan.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import android.util.Log
import org.altbeacon.beacon.*

class BeaconUtil() : InternalBeaconConsumer {
    //main 에서 권한 검사 먼저 하고 있다면 서비스 가동

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance : BeaconUtil? = null
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getInstance(_context : Context) : BeaconUtil{
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

        beaconManager = BeaconManager.getInstanceForApplication(context)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        beaconManager.bindInternal(this)

        beaconManager.removeAllRangeNotifiers()

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

        beaconManager.addRangeNotifier { beacons, region ->

        }

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