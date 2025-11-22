package com.shahbaz.farming.retrofit

import com.shahbaz.farming.datamodel.article.Data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ArticleApi {

    // Get a specific crop by ID
    @GET("/v1/crops/{id}")
    fun getCropById(
        @Path("id") cropId: Int,
        @Header("X-API-KEY") apiKey: String
    ): Call<Data>
}
