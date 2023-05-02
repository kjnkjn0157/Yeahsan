package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionBodyVO(

    @SerializedName("page")
    var page : Int,

    @SerializedName("itemCount")
    var itemCount : Int,

    @SerializedName("nextPage")
    var nextPage : Boolean,

    @SerializedName("collectionList")
    var collectionList : ArrayList<CollectionVO>

) : Parcelable
