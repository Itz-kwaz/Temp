package com.nkwachi.temp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.nkwachi.temp.weather.WeatherModel
import java.util.*


private const val TAG = "MainActivityLOG"
private const val REQUEST_CHECK_SETTINGS = 100


class MainActivity : AppCompatActivity() {


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var locationCallback: LocationCallback
    private val weatherModel: WeatherModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d(TAG, "Location is null On Activity Result")
                    parseLocation(location)
                }
            }
        }

        requestPermission()

        requestLocation()

    }

    private fun requestPermission() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    requestLocation()
                } else {
                    Log.d(TAG, "Permission denied")
                    //TODO:  Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system settings in an effort to convince the user to change their  decision.
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        Log.d(TAG, "onActivityResult: request Location")
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }
                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {

                                // Logic to handle location object
                                parseLocation(location)

                            } else {
                                val mLocationRequest = LocationRequest.create().apply {
                                    interval = 10000
                                    fastestInterval = 5000
                                    priority = LocationRequest.PRIORITY_LOW_POWER
                                    numUpdates = 1
                                }

                                fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                                fusedLocationClient.requestLocationUpdates(
                                    mLocationRequest, locationCallback,
                                    Looper.getMainLooper()
                                )
                            }
                        }
                    }
                    RESULT_CANCELED -> {
                        val toast = Toast.makeText(
                            this,
                            "Put on Location to get current weather ",
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }
        }
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                (this), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val shouldShowRequestPermission =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                //TODO: Show Alert Dialog stating why app needs location data
            }
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {

            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_LOW_POWER
            }
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(this)
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            task.addOnSuccessListener {
                Log.d(TAG, "requestLocation: Settings satisfied ")
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {

                        // Logic to handle location object
                        parseLocation(location)

                    }else{
                        Log.d(TAG, "Location is null Request Location")

                        val mLocationRequest = LocationRequest.create().apply {
                            interval = 10000
                            fastestInterval = 5000
                            priority = LocationRequest.PRIORITY_LOW_POWER
                            numUpdates = 1
                        }

                        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                        fusedLocationClient.requestLocationUpdates(
                            mLocationRequest, locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                }
            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {

                    Log.d(TAG, "EXCEPTION RESOLVABLE")
                    try {
                        exception.startResolutionForResult(
                            this@MainActivity,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        //Ignore the error
                    }
                } else {
                    Log.d(TAG, "EXCEPTION NOT RESOLVABLE")
                }
            }

        }
    }

    private fun parseLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        val address = addresses[0]
        val cityName = address.locality
        val countryName = address.countryName
        weatherModel.setCity(cityName)
        weatherModel.setCountry(countryName)
        weatherModel.getWeatherData(location.latitude, location.longitude)
    }



}
