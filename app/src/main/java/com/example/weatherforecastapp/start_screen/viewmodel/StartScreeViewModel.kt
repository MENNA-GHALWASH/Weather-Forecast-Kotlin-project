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
import com.example.weatherforecastapp.common.model.CommonRepos
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class StartScreeViewModel(private val repo: StartScreenRepo) : ViewModel() {

    private val commonRepos = CommonRepos.getInstance()

    val locationState: StateFlow<Location?> = repo.locationstate

    val _city_name =  MutableStateFlow<String>("")
    val city_name: StateFlow<String> get() = _city_name

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
            // Delay to ensure permissions are fully processed
            viewModelScope.launch {
                delay(15000) // Add slight delay to avoid race condition

                if (repo.checkLocPermission()) {
                    repo.getCurrentLoc().onEach { location ->
                        repo.locationstate.value = location
                    }.launchIn(viewModelScope)
                } else {
                    Log.e("Location", "Permission was not granted after delay, cannot fetch location")
                }
            }
        }
    }


    fun isPermissionEnabled(activity: Activity): Boolean {
        val fineLocation = ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocation = ActivityCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val result = fineLocation || coarseLocation
        Log.d("PermissionFix", "Permission enabled status: $result")

        return result
    }

    suspend fun getCityName(lat: Double, lon: Double, apiKey: String){
        val rep =  commonRepos.fetchCityName(lat, lon, apiKey)
        _city_name.value = rep
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
