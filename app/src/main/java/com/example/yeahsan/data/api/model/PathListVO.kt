package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PathListVO(

    @SerializedName("pointX")
    var pointX : String ,

    @SerializedName("pointY")
    var pointY : String

) : Parcelable
