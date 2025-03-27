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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class StartScreenRepo() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient /*= LocationServices.getFusedLocationProviderClient(application)*/
    var locationstate = mutableStateOf(Location(LocationManager.GPS_PROVIDER))



    private fun checkLocPermission(application: Application): Boolean {
        return ActivityCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }



    fun getCurrentLoc(application: Application) {
        if (!checkLocPermission(application)) return

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application)
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

    fun enableLocPermission(application: Application){
        Toast.makeText(application,"turn on location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        application.startActivity(intent)
    }

    fun isLocationEnabled(application: Application):Boolean{
        val locMngr = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locMngr.isProviderEnabled(LocationManager.GPS_PROVIDER) || locMngr.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    fun myfun(application: Application){
        if (!isLocationEnabled(application)){
            enableLocPermission(application)
        }
        else{
            getCurrentLoc(application)
        }
    }
    //we need a function to check if the location is enabled,the get current location
    //if not ,then we need to enable it through the settings -> basically the enable location
}