package com.example.yeahsan.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.databinding.ActivityPopupBinding

class PopupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPopupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        initView()

        setClickEvent()


    }

    private fun getData() {

    }

    private fun initView() {

    }

    private fun setClickEvent() {


        binding.btnPopupClose.setOnClickListener{
            finish()
        }
    }
}