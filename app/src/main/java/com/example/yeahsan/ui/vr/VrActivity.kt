package com.example.yeahsan.ui.vr

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.yeahsan.BuildConfig
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityVrBinding

class VrActivity : AppCompatActivity() {

    private lateinit var binding : ActivityVrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

        initView()
    }

    private fun getData() {

        AppDataManager(application).getSampleData {
            val vrUrl = it?.body?.vr

            initWebView(vrUrl)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(vrUrl : String?) {

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

        vrUrl?.let {
            binding.webView.loadUrl(it)
        }

        binding.webView.webChromeClient = WebChromeClient()

    }

    private fun initView() {

        supportActionBar?.title = "Vr"
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