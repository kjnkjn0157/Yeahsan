package com.example.yeahsan.ui.popup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.databinding.ActivityGameZonePopupBinding

class GameZonePopupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityGameZonePopupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameZonePopupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}