package com.example.yeahsan.ui.popup

import android.content.Intent
import android.os.Build
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.AppDataManager

import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.databinding.ActivityGameZonePopupBinding
import com.example.yeahsan.ui.ar.ArActivity


class GameZonePopupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameZonePopupBinding
    private var content: DoorListVO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameZonePopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        initView()

        setClickEvent()


    }

    private fun getData() {

        intent?.let {
            content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableExtra(AppConstants.EXTRA_ITEM, DoorListVO::class.java)
            } else {
                it.getParcelableExtra(AppConstants.EXTRA_ITEM)
            }
            Log.e("TAG", "popup in content ::: $content ")
        }
    }

    private fun initView() {

        content?.let {

            binding.tvContentTitle.text = it.hint

            val imageUrl = AppDataManager.getInstance(application as AppApplication).getFilePath() + it.image

            Glide.with(this@GameZonePopupActivity)
                .load(imageUrl)
                .into(binding.ivPopupContent)
        }

        // 게임존 팝업창이 뜨고 있으면 다른 콘텐츠를 찾아도 다른 팝업이 뜨지 않게 구분할 값
        AppDataManager.getInstance(application as AppApplication).setGamePopupResult(true)

    }

    private fun setClickEvent() {

        binding.btnGameStart.setOnClickListener {
            content?.let {
                val intent = Intent(this@GameZonePopupActivity, ArActivity::class.java)
                intent.putExtra(AppConstants.EXTRA_ITEM, it)
                activityResultLauncher.launch(intent)
            }
        }

        binding.btnPopupClose.setOnClickListener {
            finish()
        }
    }


    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val intent = it.data
            var type = ""
            if (it.resultCode == AppConstants.MISSION_ITEM_CLEAR) {
                if (intent != null) {
                    val clearItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(AppConstants.CLEAR_CONTENT, DoorListVO::class.java)
                    } else {
                        intent.getParcelableExtra(AppConstants.CLEAR_CONTENT)
                    }
                    if (clearItem != null) {
                        // 클리어된 아이템 타입 구분
                        type = if (clearItem.code.lowercase().contains(AppConstants.OUT_DOOR_TYPE)) {
                            AppConstants.OUT_DOOR_TYPE
                        } else {
                            AppConstants.IN_DOOR_TYPE
                        }
                        AppDataManager.getInstance(application as AppApplication).addMissionClearItem(clearItem ,type)
                        finish()
                    }
                }
            }
        }

    override fun onDestroy() {
        super.onDestroy()

        AppDataManager.getInstance(application as AppApplication).setGamePopupResult(false)
    }
}