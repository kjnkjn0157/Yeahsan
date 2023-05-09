package com.example.yeahsan.data.api.service

import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.CollectionListVO
import com.example.yeahsan.data.api.model.BasicDataVO
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    //예산 투어 기본 정보
    @GET(AppConstants.BASE_TOUR_LIST)
    fun getSampleData() : Call<BasicDataVO>

    //소장품 리스트
//    @GET(AppConstants.COLLECTION_LIST)
//    fun getCollectionListData() : Call<CollectionListVO>
}