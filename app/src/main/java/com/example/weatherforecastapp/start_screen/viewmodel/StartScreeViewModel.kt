package com.example.weatherforecastapp.start_screen.viewmodel

import android.app.Activity
import android.app.Application
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.core.app.ActivityCompat
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StartScreeViewModel(private val repo: StartScreenRepo) : ViewModel() {

    val locationState: StateFlow<Location?> = repo.locationstate


    fun getLocationAndPermission(activity: Activity) {
        if (!isPermissionEnabled(activity)) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1000
            )
        } else {
            // Call getCurrentLoc() ONLY if permission is granted
            if (repo.checkLocPermission()) {
                repo.getCurrentLoc().onEach { location ->
                    repo.locationstate.value = location
                }.launchIn(viewModelScope) // Make sure the Flow is collected
            } else {
                Log.e("Location", "Permission was not granted, cannot fetch location")
            }
        }
    }

    fun isPermissionEnabled(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

}

class StartScreenViewModelFactory(
    private val repo: StartScreenRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(StartScreeViewModel::class.java)) {
            return StartScreeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
