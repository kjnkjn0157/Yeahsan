package com.example.yeahsan.data.api

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.CollectionListVO
import com.example.yeahsan.data.api.model.SampleDataVO
import com.example.yeahsan.data.api.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager(private val application : Application) : ApiHelper {

    private val apiClient = ApiClient.getRetrofitService(ApiClient.getInstance(AppConstants.BASE_URL))

    private var baseData : SampleDataVO? = null
    private var collectionList : CollectionListVO? = null

    override fun getSampleData(callback: (SampleDataVO?) -> Unit) {

        if(baseData == null) {
            val call = apiClient.getSampleData()
            call.enqueue(object : Callback<SampleDataVO?>{
                override fun onResponse(call: Call<SampleDataVO?>, response: Response<SampleDataVO?>) {
                    var sampleData : SampleDataVO? = response.body()
                    sampleData?.let {
                        Log.e("TAG","sample data ::: " + it.header )
                        baseData = it
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<SampleDataVO?>, t: Throwable) {
                    Log.e("TAG","call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(baseData)
            Log.e("TAG","list callback :::")
        }
    }

    override fun getCollectionListData(callback: (CollectionListVO?) -> Unit) {
        if (collectionList == null) {
            val call = apiClient.getCollectionListData()
            call.enqueue(object : Callback<CollectionListVO?> {
                override fun onResponse(call: Call<CollectionListVO?>, response: Response<CollectionListVO?>) {
                    var collection : CollectionListVO? = response.body()
                    collection?.let{
                        Log.e("TAG","collection list ::: ${it.collectionBodyVO}")
                        collectionList = it
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<CollectionListVO?>, t: Throwable) {
                    Log.e("TAG","call onFailure ::: " + t.message.toString())
                }
            })

        } else {
            callback(collectionList)
        }
    }

}