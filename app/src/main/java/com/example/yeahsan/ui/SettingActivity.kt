package com.example.yeahsan.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yeahsan.databinding.ActivitySettingBinding
import com.google.firebase.messaging.FirebaseMessaging

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()

        clickChangeState()


        //firebase 토큰 가져오는 부분 test
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("TAG", "firebase token ::: " + task.result)
            }
        }
    }


    private fun initView() {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        setStateButton()
    }

    private fun setStateButton() {

        binding.btnAccessGps.isChecked = resultStateGps()
    }

    private fun resultStateGps(): Boolean {

        var result = false

        result = if (ContextCompat.checkSelfPermission(
                this@SettingActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this@SettingActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Log.e("TAG", "gps provider state ::: false")
                false
            } else {
                true
            }
        } else {
            Log.e("TAG", "gps permission ::: false")
            false
        }
        return result
    }

    private val activityResultLocation =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            binding.btnAccessGps.isChecked =
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    private fun clickChangeState() {

        binding.btnAccessGps.setOnClickListener {
            activityResultLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        setStateButton()
    }
}