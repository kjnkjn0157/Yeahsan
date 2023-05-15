package com.example.yeahsan.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityMainBinding
import com.example.yeahsan.firebase.MessagingService
import com.example.yeahsan.service.beacon.BeaconService
import com.example.yeahsan.ui.artifact.ArtifactActivity
import com.example.yeahsan.ui.doormissions.QuestMapActivity
import com.example.yeahsan.ui.qr.QrScannerActivity
import com.example.yeahsan.ui.questionprogress.QuestionActivity
import com.example.yeahsan.ui.setting.SettingActivity
import com.example.yeahsan.ui.vr.WebViewActivity
import com.example.yeahsan.util.OnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var pressTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.onBackPressedDispatcher.addCallback(this,callBack)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()

        setBottomSheet()

        setMoveActivity()

        checkPermission()

        firebaseTopic()

    }

    private fun initView() {

        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        binding.mainContent.btnNav.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                if (navigationClose()) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }

        })

        setNavMenuEvent()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setBottomSheet() { //공지사항

        val noticeUrl = AppDataManager.getInstance(application as AppApplication).getBaseData()?.body?.notice
        val bottomBehavior = BottomSheetBehavior.from(binding.mainContent.mainBottomSheet.root)

        binding.mainContent.mainBottomSheet.bottomWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        noticeUrl?.let {
            binding.mainContent.mainBottomSheet.bottomWebView.loadUrl(it)
        }

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

        binding.mainContent.btnGroundBottle.setOnClickListener(mainBtnClickEvent) // 유물 더 알아보기
        binding.mainContent.btnMainDiary.setOnClickListener(mainBtnClickEvent) // 실내 컨텐츠
        binding.mainContent.btnMainQuest.setOnClickListener(mainBtnClickEvent) // 나의 퀘스트
        binding.mainContent.btnMainVrPhone.setOnClickListener(mainBtnClickEvent) // vr 전시 관람
        binding.mainContent.btnMainGrabBag.setOnClickListener(mainBtnClickEvent) // 실외 컨텐츠
        binding.mainContent.btnMainQr.setOnClickListener(mainBtnClickEvent) // qr 코드
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
                    intent = Intent(this@MainActivity, WebViewActivity::class.java)
                    intent.putExtra(AppConstants.STRING_TYPE,AppConstants.VR_STRING)
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


        binding.navigationMenu.btnSetting.setOnClickListener(object : OnSingleClickListener() {
            override fun onSingleClick(v: View?) {
                intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        })

        //하위메뉴가 있을 때
        binding.navigationMenu.btnMuseumIntro.setOnClickListener { //박물관 소개
           binding.navigationMenu.lyMuseum.visibility = (application as AppApplication).visibleState(binding.navigationMenu.lyMuseum)
        }
        binding.navigationMenu.btnExhibition.setOnClickListener {//전시안내
            binding.navigationMenu.lyExhibition.visibility = (application as AppApplication).visibleState(binding.navigationMenu.lyExhibition)
        }
        binding.navigationMenu.btnMuseumAr.setOnClickListener {//예산 박물관 ar 모험
            binding.navigationMenu.lyActivityContent.visibility =  (application as AppApplication).visibleState(binding.navigationMenu.lyActivityContent)
        }

        binding.navigationMenu.btnMarketing.setOnClickListener(navWebClickListener) // 예산 홍보관
        binding.navigationMenu.btnNaePhoStory.setOnClickListener(navWebClickListener) // 내포이야기
        binding.navigationMenu.btnBobusangStory.setOnClickListener(navWebClickListener) // 보부상이야기
        binding.navigationMenu.btnCulture.setOnClickListener(navWebClickListener) //유통문화체험관
        binding.navigationMenu.btnTheater.setOnClickListener(navWebClickListener) // 4D영상관
        binding.navigationMenu.btnSpecialExhibition.setOnClickListener(navWebClickListener) // 기획전시실

        binding.navigationMenu.btnIndoor.setOnClickListener(navContentClickListener) //실내 컨텐츠
        binding.navigationMenu.btnOutdoor.setOnClickListener(navContentClickListener) // 실외 컨텐츠
        binding.navigationMenu.btnQuest.setOnClickListener(navContentClickListener) //나의 퀘스트

    }

    private val navWebClickListener = object:  OnSingleClickListener() {
        override fun onSingleClick(v: View?) {
            if (v != null) {
                intent = Intent(this@MainActivity , WebViewActivity::class.java)
                when(v.id) {
                    R.id.btn_marketing -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.MARKETING_STRING)
                    }
                    R.id.btn_nae_pho_story -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.NEAPHO_STORY_STRING)
                    }
                    R.id.btn_bobusang_story -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.BOBUSANG_STORY_STRING)
                    }
                    R.id.btn_culture -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.CULTURE_STRING)
                    }
                    R.id.btn_theater -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.THEATER_STRING)
                    }
                    R.id.btn_special_exhibition -> {
                        intent.putExtra(AppConstants.STRING_TYPE,AppConstants.SPECIAL_EXHIBITION_STRING)
                    }
                }
            }
            startActivity(intent)
        }
    }

    private val navContentClickListener = object : OnSingleClickListener() {
        override fun onSingleClick(v: View?) {
            if (v != null) {
               when(v.id) {
                   R.id.btn_indoor -> {
                       intent = Intent(this@MainActivity,QuestMapActivity::class.java)
                       intent.putExtra(AppConstants.STRING_TYPE,AppConstants.IN_DOOR_TYPE)
                   }
                   R.id.btn_outdoor -> {
                       intent = Intent(this@MainActivity,QuestMapActivity::class.java)
                       intent.putExtra(AppConstants.STRING_TYPE,AppConstants.OUT_DOOR_TYPE)
                   }
                   R.id.btn_quest -> {
                       intent = Intent(this@MainActivity, QuestionActivity::class.java)
                   }
               }
                startActivity(intent)
            }
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
            (application as AppApplication).startBeaconService(isBeaconServiceRunning()) // 비콘서비스
            (application as AppApplication).startLocationService() // 위치서비스
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(this@MainActivity, R.string.permission_denied_mention, Toast.LENGTH_SHORT).show()
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


    private fun firebaseTopic() {
        if (AppDataManager.getInstance(application as AppApplication).getFCMSubscript()) {
            MessagingService().subscribeTopic(AppConstants.FCM_TOPIC)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as AppApplication).stopBeaconService(isBeaconServiceRunning())
    }



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

//        if (!navigationClose()) {
//            if (System.currentTimeMillis() - pressTime < 1500) {
//                finishAffinity()
//            } else {
//                pressTime = System.currentTimeMillis()
//                Toast.makeText(this@MainActivity, "한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
//            }
//        }

    }


    private val callBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!navigationClose()) {
                if (System.currentTimeMillis() - pressTime < 1500) {
                    finishAffinity()
                } else {
                    pressTime = System.currentTimeMillis()
                    Toast.makeText(this@MainActivity, R.string.app_finish_mention, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigationClose() : Boolean {
        var close = false
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.close()
            close = true
        }
        return close
    }
}