package com.shahbaz.farming.datamodel.homeak

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Links(
    val self: String?,
    val plant: String?,
    val genus: String?
) : Parcelable
