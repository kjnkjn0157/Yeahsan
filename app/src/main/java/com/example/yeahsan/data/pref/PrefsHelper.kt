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
    fun setOutdoorMissionClearItems(items : ArrayList<DoorListVO>)

    fun getOutdoorMissionClearItems() : ArrayList<DoorListVO>?

    fun setIndoorMissionClearItems(items : ArrayList<DoorListVO>)

    fun getIndoorMissionClearItems() : ArrayList<DoorListVO>?

    fun addMissionClearItem(item : DoorListVO)

    //미션 미니맵 들어가기 전 다시보지않기 여부 확인 값
    fun setOutDoorIntroInvisible(result: Boolean)

    fun getOutDoorIntroInvisible() : Boolean

    fun setInDoorIntroInvisible(result: Boolean)

    fun getInDoorIntroInvisible() : Boolean



}