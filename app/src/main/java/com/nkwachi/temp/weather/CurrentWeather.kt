package com.nkwachi.temp.weather

import com.squareup.moshi.Json

data class CurrentWeather(
    @Json(name = "weather") val weather: List<Weather>,
    @Json(name = "main") val main: Main,
    @Json(name = "wind") val wind: Wind
)

data class Weather(val main: String, val description: String, val icon: String)

data class Main(val temp: Double, val pressure: Double, val humidity: Double)

data class Wind(val speed: Double, val deg: Double)
