package com.example.yeahsan.ui.questionprogress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.yeahsan.adapter.page.MissionProgressFragmentPagerAdapter
import com.example.yeahsan.databinding.ActivityQuestionBinding
import com.google.android.material.tabs.TabLayoutMediator

class QuestionActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {

        supportActionBar?.title = "나의 퀘스트"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pagerAdapter = MissionProgressFragmentPagerAdapter(supportFragmentManager,lifecycle,2)

        binding.viewPager2.adapter = pagerAdapter
        binding.viewPager2.isUserInputEnabled

        TabLayoutMediator(binding.tabs,binding.viewPager2) {tab,position ->
            val title = arrayOf("(실외)내포보부상촌","(실내)예산보부상박물관")
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