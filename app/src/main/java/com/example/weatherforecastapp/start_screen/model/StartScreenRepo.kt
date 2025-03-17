package com.example.weatherforecastapp.start_screen.model

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class StartScreenRepo(private val application: Application) {

    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
    lateinit var locationstate : MutableState<Location>


    private fun checkLocPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }



    fun getCurrentLoc() {
        if (!checkLocPermission()) return

        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    locationstate.value = location
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun enableLocPermission(){ //enable location method from tutorial
        Toast.makeText(application,"turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        application.startActivity(intent)
    }

    fun isLocationEnabled():Boolean{
        val locMngr = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locMngr.isProviderEnabled(LocationManager.GPS_PROVIDER) || locMngr.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }
}