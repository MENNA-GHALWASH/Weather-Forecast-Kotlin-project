package com.example.weatherforecastapp.start_screen.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo

class StartScreeViewModel(private val application: Application): ViewModel() {
//call repo methods
   var repo = StartScreenRepo(application)

    fun getLocationAndPermission(){
            if (!repo.isLocationEnabled()){
                repo.enableLocPermission()
            }
            else{
                repo.getCurrentLoc()
            }
        }

    fun getcurrentLoc(){
        repo.getCurrentLoc()
    }


}