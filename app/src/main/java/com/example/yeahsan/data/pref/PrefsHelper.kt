package com.example.yeahsan.data.pref

interface PrefsHelper {

    /*최조 카메라 권한 거부*/
    fun setDenyCameraPermission(result:Boolean)

    fun isDenyCameraPermission() : Boolean

}