package com.example.yeahsan.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContentVO(

    @SerializedName("version")
    val version: String?,

    @SerializedName("filePath")
    val filePath: String?,

    @SerializedName("code")
    val code: String?,

) : Parcelable
