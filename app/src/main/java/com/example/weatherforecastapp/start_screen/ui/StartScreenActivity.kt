package com.example.weatherforecastapp.start_screen.ui

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel

class StartScreenActivity : ComponentActivity() {

    val REQUEST_LOCATION_CODE = 1000

    val viewmodel = StartScreeViewModel(application)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart(){
        super.onStart()
        viewmodel.getLocationAndPermission()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_LOCATION_CODE){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                viewmodel.getcurrentLoc()
            }
        }
    }
}

//where do i build the ui?
