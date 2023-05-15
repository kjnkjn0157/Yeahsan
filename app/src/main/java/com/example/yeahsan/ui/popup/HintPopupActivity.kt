package com.example.yeahsan.ui.popup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityHintPopupBinding


class HintPopupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHintPopupBinding
    private var hint : String? = null
    private var title : String? = null
    private var imageUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHintPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        initView()

        clickEvent()
    }

    private fun getData() {

        intent?.let {
            hint = it.getStringExtra(AppConstants.HINT_STRING)
            title = it.getStringExtra(AppConstants.NAME_STRING)
            imageUrl = it.getStringExtra(AppConstants.IMAGE_URL_STRING)
        }

    }

    private fun initView() {
        binding.tvContentTitle.text = title
        binding.tvContentDesc.text = hint

        val url = AppDataManager.getInstance(application as AppApplication).getFilePath() + imageUrl
        Glide.with(this@HintPopupActivity)
            .load(url)
            .placeholder(R.drawable.img_holder)
            .into(binding.ivPopupContent)
    }

    private fun clickEvent() {

        binding.btnPopupClose.setOnClickListener{
            finish()
        }
    }


}