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

    @SerializedName("notice")
    var notice : String,

    @SerializedName("promotion")
    var promotion : String,

    @SerializedName("naepoStory")
    var naepoStory : String,

    @SerializedName("bobusangStory")
    var bobusangStory : String,

    @SerializedName("cultureExperience")
    var cultureExperience : String,

    @SerializedName("theater")
    var theater : String,

    @SerializedName("specialExhibition")
    var specialExhibition : String,

    @SerializedName("museumIntroduction")
    var museumIntroduction : String,

    @SerializedName("museumGuide")
    var museumGuide : String,

    @SerializedName("museumDirections")
    var museumDirections : String,

    @SerializedName("naepoBobusang")
    var naepoBobusang : String,

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
    var outdoorPathList : ArrayList<DoorPathListVO>,

) : Parcelable
