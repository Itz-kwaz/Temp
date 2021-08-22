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
import java.text.SimpleDateFormat
import java.util.*

enum class WeatherApiStatus { LOADING, DONE, ERROR }

private const val TAG = "WeatherModel"
private const val API_KEY = "e344dcc8b8b60513f8a76f7bff183df0"

class WeatherModel : ViewModel() {

    private val _country: MutableLiveData<String> = MutableLiveData()
    val country: LiveData<String> get() = _country

    private val _status: MutableLiveData<WeatherApiStatus> = MutableLiveData()
    val status: LiveData<WeatherApiStatus> get() = _status

    private val _weatherData  : MutableLiveData<WeatherData> = MutableLiveData()
    val weatherData: LiveData<WeatherData> = _weatherData

    private val _city: MutableLiveData<String> = MutableLiveData()
    val city: LiveData<String> get() = _city


    fun setCountry(country: String) {
        _country.value = country
    }

    fun setCity(city: String) {
        _city.value = city
    }

     fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _status.value = WeatherApiStatus.LOADING
            try {
                val listResult = WeatherApi.retrofitService.getCurrentWeatherData(
                    latitude = latitude,
                    longitude = longitude,
                    appKey = API_KEY,
                    unit = "metric",
                    exclude = "minutely,alerts"
                )

                listResult.enqueue(object : Callback<WeatherData> {
                    override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                        if(response.code() == 200) {
                            val data   = response.body()
                            if (data != null) {
                                Log.d(TAG, "onResponse: ${data.daily}")
                                _weatherData.value = data
                            }
                        }
                    }
                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
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