package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionContentVO(

    @SerializedName("idx")
    var idx : String,

    @SerializedName("title")
    var title : String,

    @SerializedName("type")
    var type : String,

    @SerializedName("url")
    var url : String,

    @SerializedName("audioExplain")
    var audioExplain : String,

    @SerializedName("createDt")
    var createDt : Long,

    @SerializedName("path")
    var path : String,

) : Parcelable
