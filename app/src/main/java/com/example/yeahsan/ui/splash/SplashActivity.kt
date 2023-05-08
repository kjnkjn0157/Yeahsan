package com.example.yeahsan.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivitySplashBinding
import com.example.yeahsan.ui.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goMain()

    }

    private fun goMain() {

        getPreData()

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000 * 2)
    }

    private fun getPreData() {

        AppDataManager.getInstance(application as AppApplication).getSampleData {

            it?.body?.filePath?.let { filePath ->
                AppDataManager.getInstance(application as AppApplication).setFilePath(filePath)
            }

            it?.let {
                AppDataManager.getInstance(application as AppApplication).setBaseData(it)
            }
        }
    }
}