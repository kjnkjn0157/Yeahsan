package com.example.yeahsan.data.api

import com.example.yeahsan.data.api.model.BasicDataVO

interface ApiHelper {

    fun getBaseData(callback: (BasicDataVO?) -> Unit)

//유물 더 알아보기 기능 _ server 미완성
 //   fun getCollectionListData(callback: (CollectionListVO?) -> Unit)
}

