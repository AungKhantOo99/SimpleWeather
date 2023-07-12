package com.testing.myweather.network

import com.testing.myweather.model.fivedayweatherinfo.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServise {

    @GET("forecast?")
    fun getWeatherData(@Query("lat") lat: String,
                @Query("lon") lon: String,
                @Query("appid") appid: String): Call<WeatherResponse>
}
