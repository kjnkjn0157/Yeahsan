package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionListVO(

    @SerializedName("header")
    var headerVO: HeaderVO,

    @SerializedName("body")
    var collectionBodyVO: CollectionBodyVO,

) : Parcelable