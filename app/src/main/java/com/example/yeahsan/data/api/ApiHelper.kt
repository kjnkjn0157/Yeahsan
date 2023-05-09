package com.example.yeahsan.data.api

import com.example.yeahsan.data.api.model.BasicDataVO

interface ApiHelper {

    fun getBaseData(callback: (BasicDataVO?) -> Unit)

 //   fun getCollectionListData(callback: (CollectionListVO?) -> Unit)
}

