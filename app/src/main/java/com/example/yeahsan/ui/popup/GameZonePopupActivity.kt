package com.example.yeahsan.ui.popup

import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager

import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.databinding.ActivityGameZonePopupBinding


class GameZonePopupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameZonePopupBinding
    private var content : DoorListVO? = null

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
            content = it.getParcelableExtra<DoorListVO>("item")
            Log.e("TAG","popup in content ::: $content ")
        }
    }

    private fun initView() {
        AppDataManager.getInstance(application as AppApplication).setGamePopupResult(true)
    }

    private fun setClickEvent() {

        binding.btnPopupClose.setOnClickListener{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        AppDataManager.getInstance(application as AppApplication).setGamePopupResult(false)
    }
}