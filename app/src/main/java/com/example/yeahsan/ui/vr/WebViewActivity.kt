package com.example.yeahsan.ui.vr

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.BuildConfig
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityVrBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVrBinding
    private var type: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()
    }

    private fun getData() {

        intent.extras?.let {
            type = it.getString(AppConstants.STRING_TYPE).toString()
        }

        type?.let { type ->
            val baseData = AppDataManager.getInstance(application as AppApplication).getBaseData()
            baseData?.let {
                when (type) {
                    AppConstants.ARTIFACT_STRING -> {
                        url = intent.extras?.getString(AppConstants.URL_STRING)
                    }
                    AppConstants.VR_STRING -> {
                        url = baseData.body.vr
                    }
                    AppConstants.MARKETING_STRING -> {
                        url = baseData.body.promotion
                    }
                    AppConstants.NEAPHO_STORY_STRING -> {
                        url = baseData.body.naepoStory
                    }
                    AppConstants.BOBUSANG_STORY_STRING -> {
                        url = baseData.body.bobusangStory
                    }
                    AppConstants.CULTURE_STRING -> {
                        url = baseData.body.cultureExperience
                    }
                    AppConstants.THEATER_STRING -> {
                        url = baseData.body.theater
                    }
                    AppConstants.SPECIAL_EXHIBITION_STRING -> {
                        url = baseData.body.specialExhibition
                    }
                }
            }

            Log.e("TAG", "url ::: $url")
            initWebView(url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(vrUrl: String?) {

        val webSetting = binding.webView.settings

        binding.webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        //  크롬에서 디버깅
        if (BuildConfig.DEBUG) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        webSetting.javaScriptEnabled = true

        webSetting.useWideViewPort = true

        webSetting.loadWithOverviewMode = true

        webSetting.setSupportMultipleWindows(true)

        webSetting.allowFileAccess = true

        webSetting.javaScriptCanOpenWindowsAutomatically = true

        vrUrl?.let {
            binding.webView.loadUrl(it)
        }

        binding.webView.webChromeClient = WebChromeClient()

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