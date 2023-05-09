package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BeaconListVO(

    @SerializedName("major")
    var major : String,

    @SerializedName("minor")
    var minor : String,

    @SerializedName("aRssi")
    var aRssi : Int,

    @SerializedName("iRssi")
    var iRssi : Int

) : Parcelable
