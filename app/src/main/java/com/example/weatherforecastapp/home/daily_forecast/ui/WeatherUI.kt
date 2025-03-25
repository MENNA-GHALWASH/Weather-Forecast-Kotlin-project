//package com.example.weatherforecastapp.home.daily_forecast.ui
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import com.example.weatherforecastapp.Data.HourlyForecast
//import com.example.weatherforecastapp.Data.WeatherResponse
//
//@Composable
//fun WeatherUI(weather: WeatherResponse?){
//    Column() {
//        if (weather != null) {
//            Text( text = weather.timezone) // center
//            Text(text = weather.hourly[0].temp.toString()) //temprature
//            //big and bold
//            Text(text = "Feels like "+weather.hourly[0].feelsLike.toString())
//
//            LazyRow() {
//                items(weather.hourly.size){
//                    hourlyWeatherColumn(weather.hourly[it])
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun hourlyWeatherColumn(hourly:HourlyForecast){
//    Column(){
//       Text( text = hourly.temp.toString())
//        Text(text =  hourly.feelsLike.toString())
//        Text( hourly.weather[0].icon) //for now
//        Text( hourly.weather[0].description)
//    }
//}//i have a feeling this ui is wrong
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.rememberImagePainter
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse

@Composable
fun WeatherUI(weather: WeatherResponse?) {
    Column {
        if (weather != null) {
            Text(text = weather.timezone) // center
            Text(text = weather.hourly[0].temp.toString()) // temperature
            // big and bold
            Text(text = "Feels like " + weather.hourly[0].feelsLike.toString())

            LazyRow {
                items(weather.hourly) { hourly ->
                    hourlyWeatherColumn(hourly)
                }
            }
        }
    }
}

@Composable
fun hourlyWeatherColumn(hourly: HourlyForecast) {
    Column {
        Text(text = hourly.temp.toString())
        Text(text = hourly.feelsLike.toString())
        Image(
            painter = rememberImagePainter("https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png"),
            contentDescription = hourly.weather[0].description
        )
        Text(text = hourly.weather[0].description)
    }
}