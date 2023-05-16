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

    @SerializedName("caption")
    var caption : String,

    @SerializedName("thumbnail")
    var thumbnail : String,

    @SerializedName("mapX")
    var mapX : String,

    @SerializedName("mapY")
    var mapY : String,

    @SerializedName("beaconList")
    var beaconList : ArrayList<BeaconListVO>,

    @SerializedName("locationList")
    var locationList : ArrayList<LocationListVO>,

) : Parcelable
