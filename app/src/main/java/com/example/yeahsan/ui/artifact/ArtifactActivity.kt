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
import com.example.yeahsan.adapter.page.ArtifactFragmentAdapter
import com.example.yeahsan.adapter.page.MissionProgressFragmentPagerAdapter
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.databinding.ActivityArtifactBinding
import com.google.android.material.tabs.TabLayoutMediator

class ArtifactActivity : AppCompatActivity() {

    private lateinit var binding : ActivityArtifactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArtifactBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        supportActionBar?.title = "유물"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pagerAdapter = ArtifactFragmentAdapter(supportFragmentManager,lifecycle,3)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled

        TabLayoutMediator(binding.tabs,binding.viewPager2) {tab,position ->
            val title = arrayOf("3D소장품","소장품","E-book소장품")
            tab.text = title[position]
        }.attach()
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