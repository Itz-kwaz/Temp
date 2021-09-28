package com.nkwachi.temp.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.nkwachi.temp.REQUEST_CHECK_SETTINGS
import javax.inject.Inject


///This class is in charge of fetching thte current Location of the user.
private const val TAG = "LocationRepository"
class LocationRepository @Inject constructor() {

    @Inject lateinit var client: SettingsClient
    @Inject lateinit var fusedLocationClient: FusedLocationProviderClient


    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_LOW_POWER
    }
    private val builder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)



    fun requestLocation(locationCalBack:(Location) -> Unit,handleException:(ResolvableApiException) ->Unit) {
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
           getLastLocation(locationCalBack)
        }

        task.addOnFailureListener { exception ->
                 if (exception is ResolvableApiException) {

                     Log.d(TAG, "EXCEPTION RESOLVABLE")
                    handleException(exception)
                 } else {
                     Log.d(TAG, "EXCEPTION NOT RESOLVABLE")
                 }
             }
    }


    @SuppressLint("MissingPermission")
    fun getLastLocation(locationCalBack:(Location) -> Unit) {

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {

                // Logic to handle location object
                locationCalBack(location)


            }else{

                val mLocationRequest = LocationRequest.create().apply {
                    interval = 10000
                    fastestInterval = 5000
                    priority = LocationRequest.PRIORITY_LOW_POWER
                    numUpdates = 1
                }

                 val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        locationResult ?: return
                        for (locations in locationResult.locations) {
                            locationCalBack(locations)
                        }
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    mLocationRequest, locationCallback,
                    Looper.getMainLooper()
                )
            }
        }



    }


}