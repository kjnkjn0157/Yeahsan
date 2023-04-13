package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DoorListVO(

    @SerializedName("seq")
    var seq : Int,

    @SerializedName("code")
    var code : String,

    @SerializedName("name")
    var name : String,

    @SerializedName("hint")
    var hint : String?,

    @SerializedName("image")
    var image : String,

    @SerializedName("mapX")
    var mapX : Int,

    @SerializedName("mapY")
    var mapY : Int,

    @SerializedName("beaconList")
    var beaconList : ArrayList<BeaconListVO>

) : Parcelable
