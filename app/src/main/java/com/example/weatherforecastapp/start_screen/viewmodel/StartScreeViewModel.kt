package com.example.weatherforecastapp.start_screen.viewmodel

import android.app.Application
import android.location.Location
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo

class StartScreeViewModel(var repo:StartScreenRepo): ViewModel() {
//call repo methods

    fun getLocationAndPermission(application: Application){
            if (!repo.isLocationEnabled(application)){
                repo.enableLocPermission(application)
            }
            else{
                repo.getCurrentLoc(application)
            }
        }

    fun getcurrentLoc(/*application: Application*/): MutableState<Location> {
        //repo.getCurrentLoc(application)
        return repo.locationstate
    }

    fun getcurrentLocVM(application: Application){
        repo.getCurrentLoc(application)
    }

    fun isPermissionEnabled(application: Application):Boolean{
        if (!repo.isLocationEnabled(application)){
            return false
        }
        else{
            return true
        }
    }

}

class StartScreenViewModelFactory(
    private val repo: StartScreenRepo
) : ViewModelProvider.Factory {

    // @Suppress("UNCHECKED_CAST")
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