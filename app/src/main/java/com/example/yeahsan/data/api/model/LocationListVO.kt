package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationListVO(

    @SerializedName("mark")
    var mark : Boolean,

    @SerializedName("latitude")
    var latitude : Float,

    @SerializedName("longitude")
    var longitude : Float,

    @SerializedName("altitude")
    var altitude : Float,

    @SerializedName("range")
    var range : Int,
) : Parcelable
