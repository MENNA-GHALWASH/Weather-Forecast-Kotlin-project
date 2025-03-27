////package com.example.weatherforecastapp.start_screen.ui
////
////import android.content.pm.PackageManager
////import android.os.Bundle
////import androidx.activity.ComponentActivity
////import androidx.lifecycle.ViewModelProvider
////import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
////import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
////import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel
////import com.example.weatherforecastapp.start_screen.viewmodel.StartScreenViewModelFactory
////
////class StartScreenActivity : ComponentActivity() {
////
////    val REQUEST_LOCATION_CODE = 1000
////
////    val repo = StartScreenRepo()
////    val factory = StartScreenViewModelFactory(repo)
////    val viewmodel = ViewModelProvider(this,factory)[StartScreeViewModel::class.java]
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////    }
////
////    override fun onStart(){
////        super.onStart()
////        viewmodel.getLocationAndPermission(this.application)
////
////    }
////
////    override fun onRequestPermissionsResult(
////        requestCode: Int,
////        permissions: Array<out String>,
////        grantResults: IntArray,
////        deviceId: Int
////    ) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
////        if (requestCode == REQUEST_LOCATION_CODE){
////            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
////                viewmodel.getcurrentLoc(this.application)
////            }
////        }
////    }
////}
////
//////where do i build the ui?
//Button(
//onClick = {
//    if (viewModel.isPermissionEnabled(application)) {
//        // Permission already granted
//        val location = viewModel.getcurrentLoc().value
//        if (location != null) {
//            LaunchedEffect(location) {
//                locVM.getCurrentWeather(
//                    location.latitude,
//                    location.longitude,
//                    apikey
//                ).collect { weather ->
//                    goToLocationOrWeather(true, weather)
//                }
//            }
//        }
//    } else {
//        // Request permission
//        (context as Activity).requestPermissions(
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//            REQUEST_LOCATION_CODE
//        )
//        goToLocationOrWeather(false, null)
//    }
//}
//) {
//    Text("Get Started")
//}