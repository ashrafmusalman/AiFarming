package com.shahbaz.farming.datamodel.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val category: String,
    val description: String,
    val growing_season: String,
    val id: Int,
    val image_url: String,
    val maturity_days: Int,
    val name: String,
    val optimal_temperature: String,
    val scientific_name: String,
    val water_requirements: String
): Parcelable