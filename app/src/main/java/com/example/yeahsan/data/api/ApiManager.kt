package com.example.yeahsan.data.api

import android.content.Context
import android.util.Log
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.SampleDataVO
import com.example.yeahsan.data.api.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager(private val context : Context) : ApiHelper {

    private val apiClient = ApiClient.getRetrofitService(ApiClient.getInstance(AppConstants.BASE_URL_TEST))

    private var sampleData : SampleDataVO? = null

    override fun getSampleData(callback: (SampleDataVO?) -> Unit) {

        if(sampleData == null) {
            val call = apiClient.getSampleData()
            call.enqueue(object : Callback<SampleDataVO?>{
                override fun onResponse(call: Call<SampleDataVO?>, response: Response<SampleDataVO?>) {
                    var sampleData : SampleDataVO? = response.body()
                    sampleData?.let {
                        Log.e("TAG","sample data ::: " + it.header )
                        sampleData = it
                        callback(sampleData)
                    }
                }

                override fun onFailure(call: Call<SampleDataVO?>, t: Throwable) {
                    Log.e("TAG","call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(sampleData)
        }
    }

}