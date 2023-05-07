package com.example.yeahsan.ui.popup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.databinding.ActivityArPopupBinding


class ARPopupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArPopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArPopupBinding.inflate(layoutInflater)
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