package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DoorPathListVO(

   @SerializedName("seq")
    var seq : Int ,

   @SerializedName("pathColor")
   var pathColor : String,

   @SerializedName("strokeColor")
   var strokeColor : String,

   @SerializedName("pointList")
   var pointList : ArrayList<PointListVO>


) : Parcelable
