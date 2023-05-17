package com.example.yeahsan.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionListVO(

    var collectionBodyVO: ArrayList<CollectionContentVO>,

    ) : Parcelable