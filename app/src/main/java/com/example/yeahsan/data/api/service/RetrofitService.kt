package com.example.yeahsan.data.api.service

import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.BasicDataVO
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.data.api.model.CollectionListVO
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    //예산 투어 기본 정보
    @GET(AppConstants.BASE_TOUR_LIST)
    fun getBaseData() : Call<BasicDataVO>

    //3d 소장품
    @GET(AppConstants.THREE_D_COLLECTION_LIST)
    fun getThreeDCollection() : Call<ArrayList<CollectionContentVO>>

    //소장품
    @GET(AppConstants.BASIC_COLLECTION_LIST)
    fun getBasicCollection() : Call<ArrayList<CollectionContentVO>>

    //e-book 소장품
    @GET(AppConstants.E_BOOK_COLLECTION_LIST)
    fun getEBookCollection() : Call<ArrayList<CollectionContentVO>>
}