package com.example.yeahsan.data

import android.app.Application
import com.example.yeahsan.data.api.ApiManager
import com.example.yeahsan.data.api.model.SampleDataVO
import com.example.yeahsan.data.pref.PrefsManager

class AppDataManager(val application: Application) : AppDataHelper {

    private val apiManager  = ApiManager(application)
    private val prefsManager: PrefsManager? = PrefsManager(application)

    override fun getSampleData(callback: (SampleDataVO?) -> Unit) {
        return apiManager.getSampleData(callback)
    }


    /*pref*/
    override fun setDenyCameraPermission(result: Boolean) {
        prefsManager?.setDenyCameraPermission(result)
    }

    override fun isDenyCameraPermission(): Boolean {
        prefsManager?.let {
            return it.isDenyCameraPermission()
        }
        return false
    }

}