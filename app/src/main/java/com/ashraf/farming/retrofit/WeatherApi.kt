package com.ashraf.farming.retrofit

import com.ashraf.farming.datamodel.weahterdatamodel.WeatherRootList
import com.ashraf.farming.util.Constant.Companion.WAETHER_API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    fun getWeatherData(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("appid") apiKey:String = WAETHER_API_KEY
    ): Call<WeatherRootList>
}