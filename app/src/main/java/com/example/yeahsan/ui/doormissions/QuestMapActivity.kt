package com.example.yeahsan.ui.doormissions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.databinding.ActivityOutsideQuestBinding


class QuestMapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOutsideQuestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOutsideQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            initView(it.getString(AppConstants.STRING_TYPE))
        }
    }

    private fun initView(type : String?) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val bundle = Bundle()

        type?.let {
            bundle.putString(AppConstants.STRING_TYPE,it)
        }

        val fragment = MiniMapFragment()
        fragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment,fragment)
        fragmentTransaction.commit()
    }
}

