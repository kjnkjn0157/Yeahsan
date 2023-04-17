package com.example.yeahsan.ui

import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.yeahsan.databinding.ActivitySettingBinding
import com.google.firebase.messaging.FirebaseMessaging

class SettingActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingBinding
    private lateinit var  locationManager : LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager


        //firebase 토큰 가져오는 부분 test
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("TAG","firebase token ::: "+task.result)
            }
        }
    }
}