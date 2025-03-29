package com.example.weatherforecastapp.start_screen.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
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
import kotlinx.coroutines.flow.*

class StartScreenRepo(private val context: Context) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationstate = mutableStateOf<Location?>(null)

    @SuppressLint("MissingPermission")
    fun getCurrentLoc(application: Application) {
        if (!checkLocPermission()) {
            Log.e("LocationError", "Permission NOT granted")
            return
        }

        if (!isLocationEnabled()) {
            Log.e("LocationError", "Location services are disabled")
            enableLocPermission()
            return
        }

        // First, try to get the last known location (faster)
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.i("LocationSuccess", "Last known location found: $location")
                locationstate.value = location
            } else {
                Log.w("LocationWarning", "Last known location is null. Requesting location updates...")

                // Fallback: Request fresh location updates
                val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000).build()

                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(result: LocationResult) {
                            result.lastLocation?.let {
                                Log.i("LocationSuccess", "Location update received: $it")
                                locationstate.value = it
                            } ?: Log.e("LocationError", "Location result is null")
                        }

                        override fun onLocationAvailability(availability: LocationAvailability) {
                            Log.i("LocationStatus", "Location available: ${availability.isLocationAvailable}")
                        }
                    },
                    Looper.getMainLooper()
                )
            }
        }.addOnFailureListener { e ->
            Log.e("LocationError", "Failed to get last known location", e)
        }
    }


    fun checkLocPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
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
