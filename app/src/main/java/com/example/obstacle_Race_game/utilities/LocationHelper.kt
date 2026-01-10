package com.example.obstacle_Race_game.utilities

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * class for handling location logic
 */
class LocationHelper(private val context: Context) {

    //fusedLocation the service for  the gps tracking
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    /**
     *the function gives the current location of the phone
     */
    @SuppressLint("MissingPermission")// They told to assume the user allows using his location
    fun fetchLocation(onDone: (lat: Double, lng: Double)  -> Unit) {
        val cts = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)//the location traking locgic
            .addOnSuccessListener { location ->
                if (location != null) {
                    onDone(location.latitude, location.longitude)
                    Log.d("LOCATION","got location!")
                } else {
                    Log.d("LOCATION", "Location is null, returning 0.0")
                    onDone(0.0, 0.0)
                }
            }
            .addOnFailureListener {
                onDone(0.0, 0.0)
            }
    }
}