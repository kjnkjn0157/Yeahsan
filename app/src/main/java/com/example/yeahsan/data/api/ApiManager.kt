package com.example.yeahsan.data.api

import android.app.Application
import android.util.Log
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.api.model.CollectionListVO
import com.example.yeahsan.data.api.model.BasicDataVO
import com.example.yeahsan.data.api.model.CollectionContentVO
import com.example.yeahsan.data.api.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiManager(private val application: Application) : ApiHelper {

    private val apiClient =
        ApiClient.getRetrofitService(ApiClient.getInstance(AppConstants.BASE_URL))

    private var baseData: BasicDataVO? = null
    private var threeDList: ArrayList<CollectionContentVO>? = null
    private var basicList: ArrayList<CollectionContentVO>? = null
    private var ebookList: ArrayList<CollectionContentVO>? = null

    override fun getBaseData(callback: (BasicDataVO?) -> Unit) {

        if (baseData == null) {
            val call = apiClient.getBaseData()
            call.enqueue(object : Callback<BasicDataVO?> {
                override fun onResponse(
                    call: Call<BasicDataVO?>,
                    response: Response<BasicDataVO?>
                ) {
                    var sampleData: BasicDataVO? = response.body()
                    sampleData?.let {
                        baseData = it
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<BasicDataVO?>, t: Throwable) {
                    Log.e("TAG", "call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(baseData)
        }
    }

    override fun getThreeDCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit) {

        if (threeDList == null) {
            val call = apiClient.getThreeDCollection()
            call.enqueue(object : Callback<ArrayList<CollectionContentVO>?> {
                override fun onResponse(
                    call: Call<ArrayList<CollectionContentVO>?>,
                    response: Response<ArrayList<CollectionContentVO>?>
                ) {
                    threeDList = response.body()
                    callback(threeDList)
                }

                override fun onFailure(call: Call<ArrayList<CollectionContentVO>?>, t: Throwable) {
                    Log.e("TAG", "call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(threeDList)
        }
    }

    override fun getBasicCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit) {

        if (basicList == null) {
            val call = apiClient.getBasicCollection()
            call.enqueue(object : Callback<ArrayList<CollectionContentVO>?> {
                override fun onResponse(
                    call: Call<ArrayList<CollectionContentVO>?>,
                    response: Response<ArrayList<CollectionContentVO>?>
                ) {
                    basicList = response.body()
                    callback(basicList)
                }

                override fun onFailure(call: Call<ArrayList<CollectionContentVO>?>, t: Throwable) {
                    Log.e("TAG", "call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(basicList)
        }
    }

    override fun getEBookCollection(callback: (ArrayList<CollectionContentVO>?) -> Unit) {

        if (ebookList == null) {
            val call = apiClient.getEBookCollection()
            call.enqueue(object : Callback<ArrayList<CollectionContentVO>?> {
                override fun onResponse(
                    call: Call<ArrayList<CollectionContentVO>?>,
                    response: Response<ArrayList<CollectionContentVO>?>
                ) {
                    ebookList = response.body()
                    callback(ebookList)
                }

                override fun onFailure(call: Call<ArrayList<CollectionContentVO>?>, t: Throwable) {
                    Log.e("TAG", "call onFailure ::: " + t.message.toString())
                }
            })
        } else {
            callback(ebookList)
        }
    }
}