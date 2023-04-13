package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleDataVO(

    @SerializedName("header")
    var header : HeaderVO ,

    @SerializedName("body")
    var body : BodyVO

) : Parcelable
