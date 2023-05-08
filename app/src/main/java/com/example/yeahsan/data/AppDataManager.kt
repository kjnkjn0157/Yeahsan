package com.example.yeahsan.data

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.AppDataManager.Companion.prefsManager
import com.example.yeahsan.data.api.ApiManager
import com.example.yeahsan.data.api.model.*
import com.example.yeahsan.data.pref.PrefsManager
import com.example.yeahsan.service.location.LocationUtil

class AppDataManager : AppDataHelper {

    companion object {
        private lateinit var apiManager : ApiManager
        @SuppressLint("StaticFieldLeak")
        private lateinit var  prefsManager : PrefsManager

        @SuppressLint("StaticFieldLeak")
        private var instance: AppDataManager? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var application : Application

        fun getInstance( appApplication: AppApplication): AppDataManager {
            return instance ?: synchronized(this) {
                instance ?: AppDataManager().also {
                    instance = it
                    application = appApplication
                    apiManager = ApiManager(application)
                    prefsManager = PrefsManager(application)
                }
            }
        }
    }


    /**
     * api*/
    override fun getSampleData(callback: (SampleDataVO?) -> Unit) {
        return apiManager.getSampleData(callback)
    }

    override fun getCollectionListData(callback: (CollectionListVO?) -> Unit) {
        return apiManager.getCollectionListData(callback)
    }

    /**
     * pref*/


    override fun setDenyCameraPermission(result: Boolean) {
        prefsManager?.setDenyCameraPermission(result)
    }

    override fun isDenyCameraPermission(): Boolean {
        prefsManager?.let {
            return it.isDenyCameraPermission()
        }
        return false
    }

    override fun setSettingFCM(result: Boolean) {
        prefsManager?.setSettingFCM(result)
    }

    override fun getSettingFCM(): Boolean {
        prefsManager?.let {
            return it.getSettingFCM()
        }
        return true
    }

    override fun setMissionClearItems(items: ArrayList<DoorListVO>) {
        prefsManager?.setMissionClearItems(items)
    }

    override fun getMissionClearItems(): ArrayList<DoorListVO>? {
        prefsManager?.let {
            return it.getMissionClearItems()
        }
        return null
    }

    override fun addMissionClearItem(item: DoorListVO) {
        prefsManager?.addMissionClearItem(item)
    }

    override fun setOutDoorIntroInvisible(result: Boolean) {
        prefsManager?.setOutDoorIntroInvisible(result)
    }

    override fun getOutDoorIntroInvisible(): Boolean {
        prefsManager?.let {
            return it.getOutDoorIntroInvisible()
        }
        return false
    }

    override fun setInDoorIntroInvisible(result: Boolean) {
       prefsManager?.setInDoorIntroInvisible(result)
    }

    override fun getInDoorIntroInvisible(): Boolean {
        prefsManager?.let {
           return it.getInDoorIntroInvisible()
        }
        return false
    }

    private var filePath : String? = null
    private var baseData : SampleDataVO? = null

    fun setBaseData(_baseData : SampleDataVO?) {
        this.baseData = _baseData
    }

    fun getBaseData() : SampleDataVO? {
        return baseData
    }
    fun setFilePath(path: String?) {
      this.filePath = path
    }

     fun getFilePath(): String? {
        return filePath
    }



}