package com.nkwachi.temp.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class WeatherApiStatus { LOADING, DONE, ERROR }

private const val TAG = "WeatherModel"

class WeatherModel : ViewModel() {

    private val _country: MutableLiveData<String> = MutableLiveData()
    val country: LiveData<String> get() = _country

    private val _status: MutableLiveData<WeatherApiStatus> = MutableLiveData()
    val status: LiveData<WeatherApiStatus> get() = _status

    private val _currentWeather: MutableLiveData<CurrentWeather> = MutableLiveData()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _city: MutableLiveData<String> = MutableLiveData()
    val city: LiveData<String> get() = _city


    fun setCountry(country: String) {
        _country.value = country
    }

    fun setCity(city: String) {
        _city.value = city
    }

    private fun getCurrentWeatherData() {
        Log.d(TAG, "getCurrentWeatherData: ")
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                val listResult = WeatherApi.retrofitService.getCurrentWeatherData(
                    latitude = "37.39",
                    longitude = "-122.08",
                    appKey = "e344dcc8b8b60513f8a76f7bff183df0"
                )

                listResult.enqueue(object : Callback<CurrentWeather> {
                    override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                        Log.d(TAG,"onResponse")
                        if(response.code() == 200) {
                            val currentWeather = response.body()
                            if (currentWeather != null) {
                                Log.d(TAG, "onResponse: Successful ${currentWeather.main.temp}")
                            }
                        }
                    }
                    override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                        Log.d(TAG, "onFailure: Failure ${t.message}")

                    }
                })


                _status.value = WeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
            }
        }
    }
}