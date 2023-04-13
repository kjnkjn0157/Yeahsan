package com.example.yeahsan.data.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MiniMapMarkersDto(

    var seq : Int,

    var lat : Float,

    var lon : Float


) : Parcelable
