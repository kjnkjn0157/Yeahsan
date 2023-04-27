package com.example.yeahsan

import android.Manifest
import android.app.PendingIntent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.altbeacon.beacon.*


class RangingActivity : AppCompatActivity(), InternalBeaconConsumer {

    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranging)


        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //권한요청
        } else {
            Log.e("TAG", "RangeActivity ::: ")
            beaconManager = BeaconManager.getInstanceForApplication(this)
            beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
            beaconManager.bindInternal(this)
        }
    }

    override fun onBeaconServiceConnect() {

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

    override fun onDestroy() {
        super.onDestroy()

        beaconManager.unbindInternal(this)
    }
}