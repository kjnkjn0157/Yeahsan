package com.example.yeahsan.data.api

import com.example.yeahsan.data.api.model.BasicDataVO
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.data.api.model.CollectionListVO

interface ApiHelper {

    //기본 정보
    fun getBaseData(callback: (BasicDataVO?) -> Unit)

    //소장품 3d
    fun getThreeDCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit)

    //소장품 - basic
    fun getBasicCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit)

    //소장품 - ebook
    fun getEBookCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit)

}

