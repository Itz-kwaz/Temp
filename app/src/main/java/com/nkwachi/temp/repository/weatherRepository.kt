package com.nkwachi.temp.repository

import com.nkwachi.temp.weather.WeatherApi
import com.nkwachi.temp.weather.WeatherData
import javax.inject.Inject

private const val API_KEY = "e344dcc8b8b60513f8a76f7bff183df0"

class WeatherRepository @Inject constructor() {

    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherData? {

        return WeatherApi.retrofitService.getCurrentWeatherData(
            latitude = latitude,
            longitude = longitude,
            appKey = API_KEY,
            unit = "metric",
            exclude = "minutely,alerts"
        )

    }
}