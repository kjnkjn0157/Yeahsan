package com.example.yeahsan.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BeaconMapVO(

    var major : Int,

    var minor : Int
) : Parcelable
