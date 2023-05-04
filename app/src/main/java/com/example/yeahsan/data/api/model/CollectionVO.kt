package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionVO(

    @SerializedName("seq")
    var seq : Int,

    @SerializedName("name")
    var name : String,

    @SerializedName("url")
    var url : String,

    @SerializedName("introduce")
    var introduce : String?,

    @SerializedName("thumbnail")
    var thumbnail : String,

    @SerializedName("image")
    var image : String

) : Parcelable
