package com.nkwachi.temp.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather?")
     fun getCurrentWeatherData(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") appKey: String
    ): Call<CurrentWeather>
}