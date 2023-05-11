package com.example.yeahsan.data.pref

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.DoorListVO
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.userAgent

class PrefsManager(private val context: Context) : PrefsHelper {

    private val preferencesName: String = context.packageName
    private val prefDenyCameraPermission: String = "$preferencesName.DENY_CAMERA"
    private val prefFcmNotice: String = "$preferencesName.FCM_NOTICE"
    private val outdoorMissionSuccess: String = "$preferencesName.OUT_MISSION_SUCCESS"
    private val indoorMissionSuccess: String = "$preferencesName.in_MISSION_SUCCESS"
    private val outdoorIntroResult : String = "$preferencesName.OUTDOOR_INTRO"
    private val indoorIntroResult : String = "$preferencesName.INDOOR_INTRO"
    private val allClearMissionIndoorResult : String = "$preferencesName.ALL_CLEAR_IN"
    private val allClearMissionOutdoorResult : String = "$preferencesName.ALL_CLEAR_OUT"
    private val subscriptState : String = "$preferencesName.FCM"
    private val arrivePopupUsing : String = "$preferencesName.ARRIVE_POPUP"

    private val scope: CoroutineScope = CoroutineScope(context = Dispatchers.Main)
    inline fun <reified T> genericType() = object : TypeToken<T>() {}.type

    //  기본 환경 변수
    private val pref: SharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)


    override fun setDenyCameraPermission(result: Boolean) {

        pref.edit().putBoolean(prefDenyCameraPermission, result).apply()
    }

    override fun isDenyCameraPermission(): Boolean {

        return pref.getBoolean(prefDenyCameraPermission, false)
    }

    override fun setSettingFCM(result: Boolean) {

        pref.edit().putBoolean(prefFcmNotice, result).apply()
    }

    override fun getSettingFCM(): Boolean {

        return pref.getBoolean(prefFcmNotice, false)
    }

    override fun setOutdoorMissionClearItems(items: ArrayList<DoorListVO>) {

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = gson.toJson(items)
        pref.edit().putString(outdoorMissionSuccess, string).apply()
    }

    override fun getOutdoorMissionClearItems(): ArrayList<DoorListVO>? {

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = pref.getString(outdoorMissionSuccess, "")
        if (string == "") {
            return arrayListOf()
        }
        return gson.fromJson(string, genericType<ArrayList<DoorListVO>>())
    }

    override fun setIndoorMissionClearItems(items: ArrayList<DoorListVO>) {
        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = gson.toJson(items)
        pref.edit().putString(indoorMissionSuccess, string).apply()
    }

    override fun getIndoorMissionClearItems(): ArrayList<DoorListVO>? {
        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = pref.getString(indoorMissionSuccess, "")
        if (string == "") {
            return arrayListOf()
        }
        return gson.fromJson(string, genericType<ArrayList<DoorListVO>>())
    }

    override fun addMissionClearItem(item: DoorListVO , type : String) {

        scope.launch {
            withContext(Dispatchers.IO) {
                var list : ArrayList<DoorListVO>? = null
                list = if (type == AppConstants.OUT_DOOR_TYPE) {
                    getOutdoorMissionClearItems()
                } else {
                    getIndoorMissionClearItems()
                }

                val allSeq : ArrayList<Int> = arrayListOf()

                list?.let {
                    if(it.size > 0) {
                        for (i in 0 until it.size) {
                            allSeq.add(it[i].seq)
                        }
                        if(allSeq.contains(item.seq)) {

                        } else {
                            it.add(item)
                            if (type == AppConstants.OUT_DOOR_TYPE) {
                                setOutdoorMissionClearItems(it)
                            } else {
                                setIndoorMissionClearItems(it)
                            }
                        }
                    } else {
                        it.add(item)
                        if (type == AppConstants.OUT_DOOR_TYPE) {
                            setOutdoorMissionClearItems(it)
                        } else {
                            setIndoorMissionClearItems(it)
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) { LocalBroadcastManager.getInstance(context.applicationContext).sendBroadcast(Intent(AppConstants.INTENT_FILTER_MISSION_ONE_CLEAR)) }
        }
    }

    override fun setOutDoorIntroInvisible(result: Boolean) {

        pref.edit().putBoolean(outdoorIntroResult, result).apply()
    }

    override fun getOutDoorIntroInvisible(): Boolean {

        return pref.getBoolean(outdoorIntroResult, false)
    }

    override fun setInDoorIntroInvisible(result: Boolean) {

        pref.edit().putBoolean(indoorIntroResult, result).apply()
    }

    override fun getInDoorIntroInvisible(): Boolean {

        return pref.getBoolean(indoorIntroResult, false)
    }

    override fun setMissionAllClearIndoor(result: Boolean) {

        if (result) {
            scope.launch {
                withContext(Dispatchers.Main) { LocalBroadcastManager.getInstance(context.applicationContext).sendBroadcast(Intent(AppConstants.INTENT_FILTER_MISSION_ONE_CLEAR)) }

            }
        }

       pref.edit().putBoolean(allClearMissionIndoorResult,result).apply()
    }

    override fun getMissionAllClearIndoor(): Boolean {

        return pref.getBoolean(allClearMissionIndoorResult ,false)
    }

    override fun setMissionAllClearOutdoor(result: Boolean) {

        if (result) {
            scope.launch {
                withContext(Dispatchers.Main) { LocalBroadcastManager.getInstance(context.applicationContext).sendBroadcast(Intent(AppConstants.INTENT_FILTER_MISSION_ONE_CLEAR)) }

            }
        }
        pref.edit().putBoolean(allClearMissionOutdoorResult,result).apply()
    }

    override fun getMissionAllClearOutdoor(): Boolean {
        return pref.getBoolean(allClearMissionOutdoorResult ,false)
    }


    override fun setFCMSubscript(subscript: Boolean) {
        pref.edit().putBoolean(subscriptState,subscript).apply()
    }

    override fun getFCMSubscript(): Boolean {
        return pref.getBoolean(subscriptState,true)
    }

    override fun setArrivePopupUse(using: Boolean) {
        pref.edit().putBoolean(arrivePopupUsing, using).apply()
    }

    override fun getArrivePopupUse(): Boolean {
        return pref.getBoolean(arrivePopupUsing,true)
    }


}