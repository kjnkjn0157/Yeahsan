package com.example.yeahsan.ui.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.yeahsan.AppApplication
import com.example.yeahsan.BuildConfig
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivitySettingBinding
import com.example.yeahsan.firebase.MessagingService
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

    }


    @SuppressLint("SetTextI18n")
    private fun initView() {

        supportActionBar?.title = "환경설정"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvAppVersion.text = AppDataManager.getInstance(application as AppApplication).getBaseData()?.body?.aVersion

        binding.tvMail.text = AppDataManager.getInstance(application as AppApplication).getBaseData()?.body?.email

        binding.btnAccessGps.isChecked = resultStateGps()

        binding.btnAccessPush.setOnClickListener(subscriptOnClickListener)

        binding.btnAccessArrive.setOnClickListener(arrivePopupClickListener)

        binding.btnAccessPush.isChecked = AppDataManager.getInstance(application as AppApplication).getFCMSubscript()

        binding.btnAccessArrive.isChecked = AppDataManager.getInstance(application as AppApplication).getArrivePopupUse()


    }


    private fun resultStateGps(): Boolean {

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        val result: Boolean = if (ContextCompat.checkSelfPermission(
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
            binding.btnAccessGps.isChecked = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

    private fun clickChangeState() {

        binding.btnAccessGps.setOnClickListener { activityResultLocation.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
    }

    private val subscriptOnClickListener = OnClickListener {
        if(!binding.btnAccessPush.isChecked) {
            MessagingService().unSubscribeTopic("jina")
            AppDataManager.getInstance(application as AppApplication).setFCMSubscript(false)
        } else {
            MessagingService().subscribeTopic("jina")
            AppDataManager.getInstance(application as AppApplication).setFCMSubscript(true)
        }
    }

    private val arrivePopupClickListener = OnClickListener {
        if (!binding.btnAccessArrive.isChecked) {
            AppDataManager.getInstance(application as AppApplication).setArrivePopupUse(false)
        } else {
            AppDataManager.getInstance(application as AppApplication).setArrivePopupUse(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}