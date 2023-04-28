package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BodyVO(

    @SerializedName("vr")
    var vr : String,

    @SerializedName("ebook")
    var ebook : String,

    @SerializedName("introduce")
    var introduce : String,

    @SerializedName("filePath")
    var filePath : String,

    @SerializedName("aContent")
    var aContent : String,

    @SerializedName("iContent")
    var iContent : String,

    @SerializedName("aVersion")
    var aVersion : String,

    @SerializedName("iVersion")
    var iVersion : String,

    @SerializedName("email")
    var email : String,

    @SerializedName("phone")
    var phone : String,

    @SerializedName("indoorMap")
    var indoorMap : String,

    @SerializedName("outdoorMap")
    var outdoorMap : String,

    @SerializedName("indoorIntro")
    var indoorIntro : String,

    @SerializedName("outdoorIntro")
    var outdoorIntro : String,

    @SerializedName("indoorList")
    var indoorList : ArrayList<DoorListVO>,

    @SerializedName("indoorPathList")
    var indoorPathList : ArrayList<DoorPathListVO>,

    @SerializedName("outdoorList")
    var outdoorList : ArrayList<DoorListVO>,

    @SerializedName("outdoorPathList")
    var outdoorPathList : ArrayList<DoorPathListVO>

) : Parcelable
