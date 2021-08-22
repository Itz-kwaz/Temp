package com.nkwachi.temp.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/onecall?")
     fun getCurrentWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appKey: String,
        @Query("units") unit: String,
        @Query("exclude") exclude: String,
    ): Call<WeatherData>
}