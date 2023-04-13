package com.example.yeahsan.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.databinding.ActivityMainBinding
import com.example.yeahsan.ui.artifact.ArtifactActivity
import com.example.yeahsan.ui.doormissions.QuestMapActivity
import com.example.yeahsan.ui.qr.QrScannerActivity
import com.example.yeahsan.ui.questionprogress.QuestionActivity
import com.example.yeahsan.ui.vr.VrActivity
import com.example.yeahsan.util.OnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()

        setBottomSheet()

        setMoveActivity()

    }

    private fun initView() {

        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

        binding.mainContent.btnNav.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
            }

        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setBottomSheet() {

        val bottomBehavior = BottomSheetBehavior.from(binding.mainContent.mainBottomSheet.root)

        binding.mainContent.mainBottomSheet.bottomWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

        binding.mainContent.mainBottomSheet.bottomWebView.loadUrl("https://www.naver.com/")

        bottomBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.mainContent.mainBottomSheet.bottomSheet.setOnClickListener(object : OnSingleClickListener(){
            override fun onSingleClick(v: View?) {
                bottomBehavior.state = if (bottomBehavior.state == BottomSheetBehavior.STATE_COLLAPSED){
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
            var intent : Intent? = null
            when(v?.id) {
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
                    intent.putExtra(AppConstants.STRING_TYPE ,AppConstants.IN_DOOR_TYPE)
                }
                R.id.btn_main_grab_bag -> {
                    intent = Intent(this@MainActivity, QuestMapActivity::class.java)
                    intent.putExtra(AppConstants.STRING_TYPE ,AppConstants.OUT_DOOR_TYPE)
                }
                R.id.btn_main_qr -> {
                    intent = Intent(this@MainActivity, QrScannerActivity::class.java)
                }
            }
            startActivity(intent)
        }
    }
}