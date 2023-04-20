package com.example.yeahsan.data.pref

import com.example.yeahsan.data.api.model.DoorListVO

interface PrefsHelper {

    /*최조 카메라 권한 거부*/
    fun setDenyCameraPermission(result:Boolean)

    fun isDenyCameraPermission() : Boolean

    //환경설정 푸시 설정
    fun setSettingFCM(result:Boolean)

    fun getSettingFCM() : Boolean

    //미션 클리어 아이템
    fun setMissionClearItems(items : ArrayList<DoorListVO>)

    fun getMissionClearItems() : ArrayList<DoorListVO>?

    fun addMissionClearItem(item : DoorListVO)

}