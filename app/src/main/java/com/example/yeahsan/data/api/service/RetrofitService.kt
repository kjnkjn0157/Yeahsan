package com.example.yeahsan.data.api.service

import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.SampleDataVO
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitService {

    //Samaple data
    @GET(AppConstants.BASE_SAMPLE_URL)
    fun getSampleData() : Call<SampleDataVO>
}