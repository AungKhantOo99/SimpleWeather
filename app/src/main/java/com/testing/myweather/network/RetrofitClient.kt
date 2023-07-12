package com.testing.myweather.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    //    https://api.openweathermap.org/data/2.5/weather?lat=19.26648&lon=96.29025&appid=c2a1a17e1d6bddfa9c3d506f2ba2ff59

//    api.openweathermap.org/data/2.5/forecast?lat=44.34&lon=10.99&appid=c2a1a17e1d6bddfa9c3d506f2ba2ff59
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .client(client)
        .build()
    val JsonApi = retrofit.create(ApiServise::class.java)


}