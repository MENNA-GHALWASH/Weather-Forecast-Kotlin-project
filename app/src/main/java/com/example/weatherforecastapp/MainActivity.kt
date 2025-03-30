package com.example.weatherforecastapp

import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import setNavHost

import androidx.compose.ui.text.buildAnnotatedString

import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text


import android.app.Activity
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel



class MainActivity : ComponentActivity() {

    private val REQUEST_LOCATION_CODE = 1000

    private val repo by lazy { StartScreenRepo(this,this) }
    private val viewmodel by lazy { StartScreeViewModel(repo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setNavHost(this.application,this,this)
        }
    }

    override fun onStart() {
        super.onStart()
        if (viewmodel.isPermissionEnabled(this)) {
            viewmodel.getLocationAndPermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)

        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewmodel.getLocationAndPermission(this)
            } else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun MainScreen(
    goToLocationOrWeather: (Boolean, WeatherResponse?) -> Unit,
    viewModel: StartScreeViewModel,
    activity: Activity,
    application:Application,
    locVM: LocationsViewModel
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rainylottie))
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF18213E), Color(0xFF923EA8)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    )

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val apikey = stringResource(R.string.geocoding_api)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier.size(300.dp),
                composition = composition,
                progress = { progress }
            )

            Text(
                text = buildAnnotatedString {
                    append("Weather ")
                    pushStyle(SpanStyle(color = Color(0xFFDDB130)))
                    append("Finder")
                },
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            val currentLocation by viewModel.locationState.collectAsState()

            LaunchedEffect(currentLocation) {
                if(currentLocation!=null){
                    val weather = locVM.getCurrentWeather(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude,
                        apikey
                    )
                    goToLocationOrWeather(true, locVM.current_weather.value)
                }
                else{
                    //make current loc not null by retriving location
                    viewModel.getLocationAndPermission(activity)
                }
            }
            //why is launchedeffect outside the button? what is it?

            Button(
                onClick = {
                    if (viewModel.isPermissionEnabled(activity)){
                        viewModel.getLocationAndPermission(activity)
                        if (currentLocation /*==*/!= null) {
                            Toast.makeText(application, "Fetching location...", Toast.LENGTH_SHORT).show()
                            viewModel.getLocationAndPermission(activity)
                            var loc = viewModel.locationState.value
                                loc?.let {
                                    locVM.getCurrentWeather(loc.latitude,
                                        it.longitude,apikey)
                                }
                            var x = locVM.current_weather.value

                            goToLocationOrWeather(true,x)
                            Log.i("getWeather", "MainScreen: x = $x")

//                            goToLocationOrWeather(true,y)
//                            Log.i("getWeather", "MainScreen: y = $y")
                        }
                        else {
                            viewModel.getLocationAndPermission(activity)
                            Toast.makeText(application, "Location retrieved is null", Toast.LENGTH_SHORT).show()
                            goToLocationOrWeather(false,null)

                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDDB130))
            ) {
                Text(text = "Get Started", color = Color.Black, fontSize = 30.sp)
            }
        }
    }
}
//permission is enabled