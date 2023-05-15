package com.example.yeahsan.ui.doormissions


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.databinding.ActivityOutsideQuestBinding


class QuestMapActivity : AppCompatActivity() {

    private lateinit var binding : ActivityOutsideQuestBinding
    private lateinit var introFragment : Fragment
    private lateinit var miniMapFragment : Fragment
    private var type : String? = null
    private var bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOutsideQuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.let {
            type = it.getString(AppConstants.STRING_TYPE)
        }

        initView()
    }

    private fun initView() {

        type?.let {
            bundle.putString(AppConstants.STRING_TYPE,it)
        }
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        introFragment = QuestIntroFragment()
        introFragment.arguments = bundle
        fragmentTransaction.add(R.id.fragment,introFragment)
        fragmentTransaction.commit()

        miniMapFragment = MiniMapFragment()
    }

     @SuppressLint("CommitTransaction")
     fun onFragmentChange(fragmentType : String) {

         if (fragmentType == AppConstants.TYPE_MINIMAP) {

            type?.let {
                bundle.putString(AppConstants.STRING_TYPE,it)
            }

            miniMapFragment.arguments = bundle

            supportFragmentManager.beginTransaction().replace(R.id.fragment, miniMapFragment)
                .commit()
        }
    }
}

