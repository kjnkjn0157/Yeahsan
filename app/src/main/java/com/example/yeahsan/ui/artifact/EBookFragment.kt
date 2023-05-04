package com.example.yeahsan.ui.artifact

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.yeahsan.BuildConfig
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.FragmentEBookBinding


class EBookFragment : Fragment() {

    private lateinit var binding : FragmentEBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        getData()
    }

    private fun getData() {
        activity?.let {
            AppDataManager(it.application).getSampleData {
                initWebView(it?.body?.ebook)
            }
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
}