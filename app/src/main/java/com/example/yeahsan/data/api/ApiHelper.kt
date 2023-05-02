package com.example.yeahsan.data.api

import com.example.yeahsan.data.api.model.CollectionListVO
import com.example.yeahsan.data.api.model.SampleDataVO

interface ApiHelper {

    fun getSampleData(callback: (SampleDataVO?) -> Unit)

    fun getCollectionListData(callback: (CollectionListVO?) -> Unit)
}

