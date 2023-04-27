package com.example.yeahsan.ui.artifact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.yeahsan.BuildConfig
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityArtifactBinding

class ArtifactActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArtifactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArtifactBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getData()

        initView()
    }

    private fun getData() {

        AppDataManager(application).getSampleData {
            initWebView(it?.body?.introduce)
        }
    }

    private fun initWebView(webUrl : String?) {

        val webSetting = binding.webView.settings

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        //  크롬에서 디버깅
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webSetting.javaScriptEnabled = true

        webSetting.useWideViewPort = true

        webSetting.setSupportMultipleWindows(true)

        webSetting.allowFileAccess = true

        webSetting.javaScriptCanOpenWindowsAutomatically = true

        webUrl?.let {
            binding.webView.loadUrl(it)
        }

        binding.webView.webChromeClient = WebChromeClient()
    }

    private fun initView() {

        supportActionBar?.title = "유물"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}