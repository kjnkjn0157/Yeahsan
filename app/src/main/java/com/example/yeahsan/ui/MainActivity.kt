package com.example.yeahsan.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityMainBinding
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.ui.artifact.ArtifactActivity
import com.example.yeahsan.ui.doormissions.QuestMapActivity
import com.example.yeahsan.ui.qr.QrScannerActivity
import com.example.yeahsan.ui.questionprogress.QuestionActivity
import com.example.yeahsan.ui.setting.SettingActivity
import com.example.yeahsan.ui.vr.VrActivity
import com.example.yeahsan.util.OnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()

        setBottomSheet()

        setMoveActivity()

        checkPermission()

        //prepareData()


    }

    private fun initView() {

        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        binding.mainContent.btnNav.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }

        })

        setNavMenuEvent()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setBottomSheet() {

        val bottomBehavior = BottomSheetBehavior.from(binding.mainContent.mainBottomSheet.root)

        binding.mainContent.mainBottomSheet.bottomWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.mainContent.mainBottomSheet.bottomWebView.loadUrl("https://www.naver.com/")

        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.mainContent.mainBottomSheet.bottomSheet.setOnClickListener(object :
            OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                bottomBehavior.state =
                    if (bottomBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                        BottomSheetBehavior.STATE_EXPANDED
                    } else
                        BottomSheetBehavior.STATE_COLLAPSED
            }
        })
    }


    private fun setMoveActivity() {

        binding.mainContent.btnGroundBottle.setOnClickListener(mainBtnClickEvent)
        binding.mainContent.btnMainDiary.setOnClickListener(mainBtnClickEvent)
        binding.mainContent.btnMainQuest.setOnClickListener(mainBtnClickEvent)
        binding.mainContent.btnMainVrPhone.setOnClickListener(mainBtnClickEvent)
        binding.mainContent.btnMainGrabBag.setOnClickListener(mainBtnClickEvent)
        binding.mainContent.btnMainQr.setOnClickListener(mainBtnClickEvent)
    }


    private val mainBtnClickEvent = object : OnSingleClickListener() {
        override fun onSingleClick(v: View?) {
            var intent: Intent? = null
            when (v?.id) {
                R.id.btn_ground_bottle -> {
                    intent = Intent(this@MainActivity, ArtifactActivity::class.java)
                }
                R.id.btn_main_quest -> {
                    intent = Intent(this@MainActivity, QuestionActivity::class.java)
                }
                R.id.btn_main_vr_phone -> {
                    intent = Intent(this@MainActivity, VrActivity::class.java)
                }
                R.id.btn_main_diary -> {
                    intent = Intent(this@MainActivity, QuestMapActivity::class.java)
                    intent.putExtra(AppConstants.STRING_TYPE, AppConstants.IN_DOOR_TYPE)
                }
                R.id.btn_main_grab_bag -> {
                    intent = Intent(this@MainActivity, QuestMapActivity::class.java)
                    intent.putExtra(AppConstants.STRING_TYPE, AppConstants.OUT_DOOR_TYPE)
                }
                R.id.btn_main_qr -> {
                    intent = Intent(this@MainActivity, QrScannerActivity::class.java)
                }
            }
            startActivity(intent)
        }
    }

    private fun setNavMenuEvent() {

        var intent: Intent? = null

        binding.navigationMenu.btnSetting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        binding.navigationMenu.btnMuseumIntro.setOnClickListener {
           binding.navigationMenu.lyMuseum.visibility = (application as AppApplication).visibleState(binding.navigationMenu.lyMuseum)
        }

        binding.navigationMenu.btnExhibition.setOnClickListener {
            binding.navigationMenu.lyExhibition.visibility = (application as AppApplication).visibleState(binding.navigationMenu.lyExhibition)
        }

        binding.navigationMenu.btnMuseumAr.setOnClickListener {
            binding.navigationMenu.lyActivityContent.visibility =  (application as AppApplication).visibleState(binding.navigationMenu.lyActivityContent)
        }
    }

    //    @RequiresApi(Build.VERSION_CODES.S)
//    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermission() {
        var permissions: Array<String> = arrayOf(
            //  android 12
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions += Manifest.permission.BLUETOOTH_SCAN
            permissions += Manifest.permission.BLUETOOTH_ADVERTISE
            permissions += Manifest.permission.BLUETOOTH_CONNECT
        }

        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setPermissions(*permissions)
            .check()
    }

    private val permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Log.e("permission", "::: permissionGranted")
            (application as AppApplication).startBeaconService(isBeaconServiceRunning())
            (application as AppApplication).startLocationService()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Log.e("permission", "::: permissionDenied")
            Toast.makeText(this@MainActivity, "서비스가 제한될 수 있습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isBeaconServiceRunning(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (BeaconService::class.java.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    private fun prepareData() { //todo splash class 로 빼기

        AppDataManager.getInstance(application as AppApplication).getSampleData {
            it?.body?.filePath?.let { filePath ->
                AppDataManager.getInstance(application as AppApplication).setFilePath(filePath)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy ::: ")
        (application as AppApplication).stopBeaconService(isBeaconServiceRunning())
    }
}