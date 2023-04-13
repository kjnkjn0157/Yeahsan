package com.example.yeahsan.data.api.retrofit

import com.example.yeahsan.data.api.service.RetrofitService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null
    var dataHelper: RetrofitService? = null

    val gson = GsonBuilder().setLenient().create()

    fun getInstance(baseUrl : String) : Retrofit {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        } else {
            return retrofit!!
        }
        return retrofit!!
    }

    fun getRetrofitService(retrofit: Retrofit?) : RetrofitService {
        dataHelper = retrofit?.create(RetrofitService::class.java)

        return dataHelper as RetrofitService
    }
}