package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HeaderVO(

    @SerializedName("result")
    var result : Boolean,

    @SerializedName("code")
    var code : String,

    @SerializedName("message")
    var message : String,

) : Parcelable
