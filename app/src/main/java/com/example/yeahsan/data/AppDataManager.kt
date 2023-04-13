package com.example.yeahsan.data

import android.app.Application
import com.example.yeahsan.data.api.ApiManager
import com.example.yeahsan.data.api.model.SampleDataVO

class AppDataManager(val application: Application) : AppDataHelper {

    private val apiManager  = ApiManager(application)

    override fun getSampleData(callback: (SampleDataVO?) -> Unit) {
        return apiManager.getSampleData(callback)
    }
}