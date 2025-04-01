//package com.example.weatherforecastapp.start_screen.model
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.app.Application
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationManager
//import android.os.Looper
//import android.provider.Settings
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.runtime.mutableStateOf
//import androidx.core.app.ActivityCompat
//import com.google.android.gms.location.*
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.*
//
//class StartScreenRepo(private val context: Context, private val activity: Activity) {
//    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
//    val locationstate = MutableStateFlow<Location?>(null)
//    private var locationCallback: LocationCallback? = null
//
//
//    // i have no idea what to do?
//    @SuppressLint("MissingPermission")
//    fun getCurrentLoc(): Flow<Location> = callbackFlow {
//        if (!checkLocPermission()) {
//            Log.e("LocationError", "Permission NOT granted")
//            close()
//            return@callbackFlow
//        }
//
//        if (!isLocationEnabled()) {
//            Log.e("LocationError", "Location services are disabled")
//            enableLocPermission()
//            close()
//            return@callbackFlow
//        }
//
//        val callback = object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                result.lastLocation?.let { location ->
//                    trySend(location)
//
//                }
//            }
//        }
//        locationCallback = callback
//
//        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
//            if (location != null) {
//                trySend(location)
//            }
//        }
//
//        val locationRequest = LocationRequest.Builder(
//            Priority.PRIORITY_HIGH_ACCURACY,
//            15000
//        ).build()
//
//        fusedLocationProviderClient.requestLocationUpdates(
//            locationRequest,
//            callback,
//            Looper.getMainLooper()
//        )
//
//        awaitClose {
//            locationCallback?.let {
//                fusedLocationProviderClient.removeLocationUpdates(it)
//            }
//        }
//    }.onEach { location ->
//        locationstate.value = location
//    }
//
//    fun stopLocationUpdates() {
//        locationCallback?.let {
//            fusedLocationProviderClient.removeLocationUpdates(it)
//            locationCallback = null
//        }
//    }
//
//    fun checkLocPermission(): Boolean {
//        return ActivityCompat.checkSelfPermission(
//            activity,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//    fun enableLocPermission() {
//        Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
//        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//        context.startActivity(intent)
//    }
//
//    fun isLocationEnabled(): Boolean {
//        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
//                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//}
package com.example.weatherforecastapp.start_screen.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class StartScreenRepo(private val context: Context, private val activity: Activity) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationstate = MutableStateFlow<Location?>(null)
    private var locationCallback: LocationCallback? = null


    @SuppressLint("MissingPermission")
    fun getCurrentLoc() = callbackFlow {
        if (!checkLocPermission()) {
            Log.e("LocationError", "Permission NOT granted")
            close()
            return@callbackFlow
        }

        if (!isLocationEnabled()) {
            Log.e("LocationError", "Location services are disabled")
            enableLocPermission()
            close()
            return@callbackFlow
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            5000 // Fetch every 5 seconds for testing
        ).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(location)
                    Log.d("LocationUpdate", "New location: ${location.latitude}, ${location.longitude}")
                } ?: run {
                    Log.e("LocationUpdate", "Location result is null")
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                Log.d("LocationUpdate", "Location availability: ${availability.isLocationAvailable}")
            }
        }
        locationCallback = callback

        // First, check last known location
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                trySend(location)
            } else {
                // If lastLocation is null, request updates
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    callback,
                    Looper.getMainLooper()
                )
            }
        }

        awaitClose {
            locationCallback?.let {
                fusedLocationProviderClient.removeLocationUpdates(it)
            }
        }
    }


    fun stopLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }

    fun checkLocPermission(): Boolean {
        val fineLocation = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("PermissionDebug", "Fine Location Permission: $fineLocation")
        Log.d("PermissionDebug", "Coarse Location Permission: $coarseLocation")

        if (!fineLocation) {
            Log.w("PermissionDebug", "FINE location missing! Requesting again...")
            requestFineLocation()
        }

        return fineLocation || coarseLocation
    }

    fun requestFineLocation() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1001 // Different request code for fine location
        )
    }


    fun enableLocPermission() {
        Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


}