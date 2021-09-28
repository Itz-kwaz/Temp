package com.nkwachi.temp.weather

import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ResolvableApiException
import com.nkwachi.temp.repository.LocationRepository
import com.nkwachi.temp.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


enum class WeatherApiStatus { LOADING, DONE, ERROR }

private const val TAG = "WeatherModel"

@HiltViewModel
class WeatherModel @Inject constructor(): ViewModel() {

    @Inject lateinit var weatherRepository: WeatherRepository
    @Inject lateinit var geocoder:Geocoder
    @Inject lateinit var locationRepository: LocationRepository

    private val _country: MutableLiveData<String> = MutableLiveData()
    val country: LiveData<String> get() = _country
    var locationPermission = false;

    private val _status: MutableLiveData<WeatherApiStatus> = MutableLiveData()
    val status: LiveData<WeatherApiStatus> get() = _status

    private val _weatherData  : MutableLiveData<WeatherData> = MutableLiveData()
    val weatherData: LiveData<WeatherData> = _weatherData

    private val _city: MutableLiveData<String> = MutableLiveData()
    val city: LiveData<String> get() = _city


    init{
        _status.value = WeatherApiStatus.LOADING
        Log.d(TAG, ": Location permissison is $locationPermission")
    }



     private fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
               val data = weatherRepository.getWeatherData(latitude,longitude)
                if (data != null) {
                    _weatherData.value = data
                    _status.value = WeatherApiStatus.DONE
                }

            } catch (e: Exception) {
                _status.value = WeatherApiStatus.ERROR
            }
        }
    }

    fun requestLocation(handleException:(ResolvableApiException) -> Unit) {
        locationRepository.requestLocation({location ->
            parseLocation(location)
        },{
            handleException(it)
        })
    }

  private fun parseLocation(location: Location) {
        try{
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val address = addresses[0]
            _city.value = address.locality
            _country.value = address.countryName
        }catch(e: Exception){

        }
        getWeatherData(location.latitude, location.longitude)
    }


    fun getLastLocation() {
        locationRepository.getLastLocation{
            parseLocation(it)
        }
    }


}