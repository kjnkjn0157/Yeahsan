package com.example.yeahsan.data.pref

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.BodyVO
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.data.api.model.HeaderVO
import com.example.yeahsan.data.api.model.SampleDataVO
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PrefsManager(private val context: Context) : PrefsHelper {

    private val preferencesName: String = context.packageName
    private val prefDenyCameraPermission: String = "$preferencesName.DENY_CAMERA"
    private val prefFcmNotice: String = "$preferencesName.FCM_NOTICE"
    private val missionSuccess: String = "$preferencesName.MISSION_SUCCESS"
    private val outdoorIntroResult : String = "$preferencesName.OUTDOOR_INTRO"
    private val indoorIntroResult : String = "$preferencesName.INDOOR_INTRO"

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

    override fun setMissionClearItems(items: ArrayList<DoorListVO>) {

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = gson.toJson(items)
        pref.edit().putString(missionSuccess, string).apply()
    }

    override fun getMissionClearItems(): ArrayList<DoorListVO>? {

        val gson = GsonBuilder().enableComplexMapKeySerialization().create()
        val string = pref.getString(missionSuccess, "")
        if (string == "") {
            return arrayListOf()
        }
        return gson.fromJson(string, genericType<ArrayList<DoorListVO>>())
    }

    override fun addMissionClearItem(item: DoorListVO) {

        scope.launch {
            withContext(Dispatchers.IO) {
                val list = getMissionClearItems()
                val allSeq : ArrayList<Int> = arrayListOf()

                list?.let {
                    if(it.size > 0) {
                        for (i in 0 until it.size) {
                            allSeq.add(it[i].seq)
                        }
                        if(allSeq.contains(item.seq)) {

                        } else {
                            it.add(item)
                            setMissionClearItems(it)
                        }
                    } else {
                        it.add(item)
                        setMissionClearItems(it)
                    }
                }
            }
            withContext(Dispatchers.Main) {
                LocalBroadcastManager.getInstance(context.applicationContext).sendBroadcast(Intent(AppConstants.INTENT_FILTER_MISSION_ONE_CLEAR))
            }
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



}