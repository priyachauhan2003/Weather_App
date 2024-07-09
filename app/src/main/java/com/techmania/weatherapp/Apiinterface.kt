package com.techmania.weatherapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Apiinterface {

    @GET("weather")
    fun getWeatherData(
        @Query("q") city:String,
        @Query("app_id") app_id:String,
        @Query("units") units:String
    ): Call<WeatherApp>
}