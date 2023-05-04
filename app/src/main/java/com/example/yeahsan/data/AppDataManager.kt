package com.example.yeahsan.data

import android.app.Application
import com.example.yeahsan.data.api.ApiManager
import com.example.yeahsan.data.api.model.CollectionListVO
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.data.api.model.SampleDataVO
import com.example.yeahsan.data.pref.PrefsManager

class AppDataManager(val application: Application) : AppDataHelper {

    private val apiManager  = ApiManager(application)
    private val prefsManager: PrefsManager? = PrefsManager(application)

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

    override fun setFilePath(path: String?) {
       prefsManager?.setFilePath(path)
    }

    override fun getFilePath(): String? {
        prefsManager?.let{
           return it.getFilePath()
        }
        return null
    }

}