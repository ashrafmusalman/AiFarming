package com.ashraf.farming.datamodel

import androidx.annotation.DrawableRes

data class Article(
    val id: String,
    val title: String,
    @DrawableRes val image: Int
)
