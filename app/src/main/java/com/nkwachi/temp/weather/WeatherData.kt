package com.nkwachi.temp.weather

import com.squareup.moshi.Json

private const val iconBaseUrl = "http://openweathermap.org/img/wn"
data class WeatherData(
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
)

data class CurrentWeather(
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "temp") val temperature: Double,
    val humidity: Double,
    val weather: List<Weather>,
)

data class HourlyWeather(
    @Json(name = "dt") val dateTime: Long,
    val humidity: Double,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "temp") val temperature: Double,
    val weather: List<Weather>,
)

data class DailyWeather(
    @Json(name = "dt") val dateTime: Long,
    val weather: List<Weather>,
    val humidity: Double,
    @Json(name = "wind_speed") val windSpeed: Double,
    @Json(name = "temp") val temperature: Temperature
    )

data class Weather(@Json(name = "main") val description: String, var icon: String) {
    init {
        icon = "$iconBaseUrl/$icon@2x.png"
    }
}

data class Temperature(
    val min: Double,
    val max: Double
)

