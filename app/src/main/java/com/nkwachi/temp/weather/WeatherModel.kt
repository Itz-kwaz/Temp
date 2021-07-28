package com.nkwachi.temp.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

enum class WeatherApiStatus{LOADING, DONE, ERROR}

class WeatherModel : ViewModel() {

    private val _country: MutableLiveData<String> = MutableLiveData()
    val country: LiveData<String> get() = _country

    private val _city: MutableLiveData<String> = MutableLiveData()
    val city :  LiveData<String> get() = _city


    fun setCountry(country: String) {
        _country.value = country
    }

    fun setCity(city: String) {
        _city.value = city
    }
}