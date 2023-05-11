package com.example.yeahsan.data

import android.annotation.SuppressLint
import android.app.Application
import com.example.yeahsan.AppApplication
import com.example.yeahsan.data.api.ApiManager
import com.example.yeahsan.data.api.model.*
import com.example.yeahsan.data.pref.PrefsManager

class AppDataManager : AppDataHelper {

    private var filePath : String? = null
    private var baseData : BasicDataVO? = null
    private var gamePopupResult  = false
    private var checkItemMap : HashMap<String,String> = hashMapOf()

    companion object {
        private lateinit var apiManager : ApiManager
        @SuppressLint("StaticFieldLeak")
        private lateinit var  prefsManager : PrefsManager

        @SuppressLint("StaticFieldLeak")
        private var instance: AppDataManager? = null

        @SuppressLint("StaticFieldLeak")
        private lateinit var application : Application

        fun getInstance(appApplication: AppApplication): AppDataManager {
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
    override fun getBaseData(callback: (BasicDataVO?) -> Unit) {
        return apiManager.getBaseData(callback)
    }

// 우물 더 알아보기 기능 사용
//    override fun getCollectionListData(callback: (CollectionListVO?) -> Unit) {
//        return apiManager.getCollectionListData(callback)
//    }


    /**
     * pref*/


    override fun setDenyCameraPermission(result: Boolean) {
        prefsManager.setDenyCameraPermission(result)
    }

    override fun isDenyCameraPermission(): Boolean {
        prefsManager.let {
            return it.isDenyCameraPermission()
        }
    }

    override fun setSettingFCM(result: Boolean) {
        prefsManager.setSettingFCM(result)
    }

    override fun getSettingFCM(): Boolean {
        prefsManager.let {
            return it.getSettingFCM()
        }
    }

    override fun setOutdoorMissionClearItems(items: ArrayList<DoorListVO>) {
        prefsManager.setOutdoorMissionClearItems(items)
    }

    override fun getOutdoorMissionClearItems(): ArrayList<DoorListVO>? {
        prefsManager.let {
            return it.getOutdoorMissionClearItems()
        }
    }

    override fun setIndoorMissionClearItems(items: ArrayList<DoorListVO>) {
        prefsManager.setIndoorMissionClearItems(items)
    }

    override fun getIndoorMissionClearItems(): ArrayList<DoorListVO>? {
        prefsManager.let {
            return it.getIndoorMissionClearItems()
        }
    }

    override fun addMissionClearItem(item: DoorListVO , type : String) {
        prefsManager.addMissionClearItem(item,type)
    }

    override fun setOutDoorIntroInvisible(result: Boolean) {
        prefsManager.setOutDoorIntroInvisible(result)
    }

    override fun getOutDoorIntroInvisible(): Boolean {
        prefsManager.let {
            return it.getOutDoorIntroInvisible()
        }
    }

    override fun setInDoorIntroInvisible(result: Boolean) {
       prefsManager.setInDoorIntroInvisible(result)
    }

    override fun getInDoorIntroInvisible(): Boolean {
        prefsManager.let {
           return it.getInDoorIntroInvisible()
        }
    }

    override fun setMissionAllClearIndoor(result: Boolean) {
        prefsManager.setMissionAllClearIndoor(result)
    }

    override fun getMissionAllClearIndoor(): Boolean {
        prefsManager.let {
            return it.getMissionAllClearIndoor()
        }
    }

    override fun setMissionAllClearOutdoor(result: Boolean) {
        prefsManager.setMissionAllClearOutdoor(result)
    }

    override fun getMissionAllClearOutdoor(): Boolean {
        prefsManager.let {
            return it.getMissionAllClearOutdoor()
        }
    }

    override fun setFCMSubscript(subscript: Boolean) {
        prefsManager.setFCMSubscript(subscript)
    }

    override fun getFCMSubscript(): Boolean {
        prefsManager.let {
            return it.getFCMSubscript()
        }
    }

    override fun setArrivePopupUse(using: Boolean) {
        prefsManager.setArrivePopupUse(using)
    }

    override fun getArrivePopupUse(): Boolean {
        return prefsManager.getArrivePopupUse()
    }

    fun setBaseData(_baseData : BasicDataVO?) {
        this.baseData = _baseData
    }

    fun getBaseData() : BasicDataVO? {
        return baseData
    }
    fun setFilePath(path: String?) {
      this.filePath = path
    }

     fun getFilePath(): String? {
        return filePath
    }

    fun setGamePopupResult(result: Boolean) {
        this.gamePopupResult = result
    }

    fun isGamePopupResult() : Boolean {
        return gamePopupResult
    }

    fun setCheckBeaconFindItem(code : String) {
        checkItemMap[code] = code
    }

    fun getCheckBeaconFindItem() : HashMap<String,String> {
         return checkItemMap
    }

    fun clearCheckBeaconMap() {
        checkItemMap.clear()
    }
}