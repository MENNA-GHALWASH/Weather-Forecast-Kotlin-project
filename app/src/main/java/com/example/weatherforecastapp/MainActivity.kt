package com.example.weatherforecastapp

import android.app.Application
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.compose.LottieAnimatable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreenViewModelFactory
import com.example.weatherforecastapp.ui.theme.WeatherForecastAppTheme
import setNavHost

class MainActivity : ComponentActivity() {

    val REQUEST_LOCATION_CODE = 1000

//    val repo = StartScreenRepo()
//    val factory = StartScreenViewModelFactory(repo)
//    val viewmodel = ViewModelProvider(this,factory)[StartScreeViewModel::class.java]

    val repo = StartScreenRepo()
    val viewmodel = StartScreeViewModel(repo)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
          //  MainScreen()
            setNavHost(this.application)
        }
    }

    override fun onStart(){
        super.onStart()
        viewmodel.getLocationAndPermission(this.application)

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
                viewmodel.getcurrentLocVM(this.application)
            }
        }
    }

}

@Composable
fun MainScreen(goToLocationOrWeather: (isLocationsEnabled:Boolean,weatherResp:WeatherResponse?) -> Unit ,viewModel:StartScreeViewModel,application:Application, locVM:LocationsViewModel) {

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.rainylottie))

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF18213E), Color(0xFF923EA8)),
        startY = 0f,
        endY = Float.POSITIVE_INFINITY
    ) // will have to extract you in a color file later, make it theme dark

    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever // Infinite looping
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
                progress = {progress}
            )
            Text(
                text = buildAnnotatedString {
                    append("Weather ")
                    pushStyle(SpanStyle(color = Color(0xFFDDB130))) // Yellow color for "Checker"
                    append("Finder")
                },
                fontSize = 70.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.getLocationAndPermission(application)
                    var flag = viewModel.isPermissionEnabled(application)
                    var loc = viewModel.getcurrentLoc().value
                    var resp = locVM.getCurrentWeather(loc.latitude,loc.longitude,apikey )

                    // Navigate to:
                    // WeatherScreen of selected location if available
                    // Otherwise, navigate to select location
                    //i am really really not sure of this: locVM.current_weather.value
                    goToLocationOrWeather(flag , locVM.current_weather.value) //was false for testing
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDDB130)
                )
            ) {
                Text(text = "Get Started", color = Color.Black , fontSize = 30.sp)
            }
        }
    }
}
//                Text(text = "Get Started", color = Color.Black , fontSize = 30.sp)