package com.example.yeahsan.ui.vr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.R
import com.example.yeahsan.databinding.ActivityVrBinding

class VrActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVrBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}