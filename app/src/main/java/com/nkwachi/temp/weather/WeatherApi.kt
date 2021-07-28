package com.nkwachi.temp.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val BASE_URL =
    "https://api.openweathermap.org/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object WeatherApi {
    val retrofitService:  WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }
}