package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.android.material.appbar.AppBarLayout.LayoutParams.ScrollEffect
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointListVO(

    @SerializedName("pointX")
    var pointX : Int,

    @SerializedName("pointY")
    var pointY : Int,

) : Parcelable
