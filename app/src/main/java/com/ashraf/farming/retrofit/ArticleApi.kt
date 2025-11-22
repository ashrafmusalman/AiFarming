package com.ashraf.farming.retrofit

import com.ashraf.farming.datamodel.article.Data
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
