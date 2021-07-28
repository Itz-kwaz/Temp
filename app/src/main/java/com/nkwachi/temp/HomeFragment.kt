package com.nkwachi.temp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.nkwachi.temp.databinding.FragmentHomeBinding
import com.nkwachi.temp.weather.WeatherModel
import java.util.*


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {


    private val weatherModel: WeatherModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    requestLocation()
                } else {
                    Log.d(TAG, "Permission denied")
                    //TODO:
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.

                }
            }

        requestLocation()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.viewModel = weatherModel
        binding.lifecycleOwner = this
        return binding.root
    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                (context)!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            Log.d(TAG, "No permission granted")
            val shouldShowRequestPermission =
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
            Log.d(TAG, "Value: $shouldShowRequestPermission")

            //TODO: Show Alert Dialog stating why app needs location data

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {

                    // Logic to handle location object
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    val address = addresses[0]
                    val cityName = address.locality
                    val countryName = address.countryName
                    weatherModel.setCity(cityName)
                    weatherModel.setCountry(countryName)


                    Log.d(TAG, "onSuccess: $cityName$countryName")
                } else {
                    Log.d(TAG, "Location is null")
                }
            }
        }
    }



}