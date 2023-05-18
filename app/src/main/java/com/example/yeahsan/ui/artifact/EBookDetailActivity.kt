package com.example.yeahsan.ui.artifact

import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.yeahsan.AppConstants
import com.example.yeahsan.R
import com.example.yeahsan.adapter.EBookDetailAdapter
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.databinding.ActivityEbookDetailBinding
import karacken.curl.PageCurlAdapter
import karacken.curl.PageSurfaceView
import java.lang.reflect.Field


class EBookDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEbookDetailBinding
    private var content: CollectionContentVO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEbookDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getData()

    }

    private fun getData() {

        intent?.let {
            content = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelableExtra(AppConstants.E_BOOK_ITEM, CollectionContentVO::class.java)
            } else {
                it.getParcelableExtra(AppConstants.E_BOOK_ITEM)
            }
        }

        content?.let {
            val page = it.url

            val imageList: ArrayList<String> = arrayListOf()

            val drawables: Array<Field> = R.drawable::class.java.fields

            for (i in drawables) {
                try {
                    if (i.name.contains(page)) {
                        val drawableName = i.name
                        imageList.add(drawableName)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            setData(imageList)
        }
    }


    private fun setData(list : ArrayList<String>) {
        binding.viewPager2.adapter = EBookDetailAdapter(this@EBookDetailActivity,list)
    }
}
