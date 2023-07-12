package com.testing.myweather.model.fivedayweatherinfo

data class WeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Detail>,
    val message: Int
) : java.io.Serializable