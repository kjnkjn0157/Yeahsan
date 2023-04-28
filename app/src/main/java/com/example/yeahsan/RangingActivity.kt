package com.example.yeahsan

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.yeahsan.databinding.ActivityRangingBinding
import com.example.yeahsan.service.BeaconService


class RangingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRangingBinding

    //val beconservice = BeaconServiceUtil.getInstance(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRangingBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (ActivityCompat.checkSelfPermission(
                this,
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

           // startBeaconService()

            //beconservice.onBeaconServiceConnect()

        }

        binding.btnStop.setOnClickListener {
           // stopBeaconService()
        }

    }



}