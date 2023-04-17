package com.example.yeahsan.data.pref

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(private val context : Context) : PrefsHelper {

    private val preferencesName: String = context.packageName

    private val prefDenyCameraPermission : String = "$preferencesName.DENY_CAMERA"

    //  기본 환경 변수
    private val pref: SharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    override fun setDenyCameraPermission(result : Boolean) {

        pref.edit().putBoolean(prefDenyCameraPermission ,result).apply()
    }

    override fun isDenyCameraPermission(): Boolean {
        var isDeny = false
        isDeny = pref.getBoolean(prefDenyCameraPermission,false)
        return isDeny
    }
}