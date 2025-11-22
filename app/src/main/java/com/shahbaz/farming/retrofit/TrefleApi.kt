package com.shahbaz.farming.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.shahbaz.farming.datamodel.homeak.homeSelfResponse
import com.shahbaz.farming.util.Constant.Companion.TREFLE_TOKEN

interface TrefleApi {

    @GET("plants")
    suspend fun getPlants(
        @Query("token") token: String = TREFLE_TOKEN
    ): Response<homeSelfResponse>

    @GET("plants/search")
    suspend fun searchPlants(
        @Query("q") query: String,
        @Query("token") token: String = TREFLE_TOKEN
    ): Response<homeSelfResponse>
}
