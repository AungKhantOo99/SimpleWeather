package com.testing.myweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.internal.ApiKey
import com.testing.myweather.apiKey
import com.testing.myweather.model.fivedayweatherinfo.WeatherResponse
import com.testing.myweather.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel:ViewModel() {
    lateinit var alldata: MutableLiveData<WeatherResponse>
//    private val _apiResponse = MutableLiveData<WeatherResponse>()
//    val apiResponse: LiveData<WeatherResponse> = _apiResponse

    fun getWeatherData(lat:String,lon:String) : LiveData<WeatherResponse> {
        if(! :: alldata.isInitialized){
            alldata= MutableLiveData()
            val getapi= RetrofitClient.JsonApi.getWeatherData(lat,lon, apiKey)
            getapi.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>,response: Response<WeatherResponse>)
                {
                    Log.d("Hello",response.body().toString())
                    alldata.value=response.body()
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {

                }
            })
        }
        return alldata
    }
}
