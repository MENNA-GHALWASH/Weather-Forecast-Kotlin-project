////package com.example.weatherforecastapp.home.daily_forecast.ui
////
////import androidx.compose.foundation.layout.Column
////import androidx.compose.foundation.layout.Row
////import androidx.compose.foundation.lazy.LazyRow
////import androidx.compose.material3.Text
////import androidx.compose.runtime.Composable
////import com.example.weatherforecastapp.Data.HourlyForecast
////import com.example.weatherforecastapp.Data.WeatherResponse
////
////@Composable
////fun WeatherUI(weather: WeatherResponse?){
////    Column() {
////        if (weather != null) {
////            Text( text = weather.timezone) // center
////            Text(text = weather.hourly[0].temp.toString()) //temprature
////            //big and bold
////            Text(text = "Feels like "+weather.hourly[0].feelsLike.toString())
////
////            LazyRow() {
////                items(weather.hourly.size){
////                    hourlyWeatherColumn(weather.hourly[it])
////                }
////            }
////        }
////    }
////}
////
////@Composable
////fun hourlyWeatherColumn(hourly:HourlyForecast){
////    Column(){
////       Text( text = hourly.temp.toString())
////        Text(text =  hourly.feelsLike.toString())
////        Text( hourly.weather[0].icon) //for now
////        Text( hourly.weather[0].description)
////    }
////}//i have a feeling this ui is wrong
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import coil.compose.rememberImagePainter
//import com.example.weatherforecastapp.Data.HourlyForecast
//import com.example.weatherforecastapp.Data.WeatherResponse
//
//@Composable
//fun WeatherUI(weather: WeatherResponse?) {
//    Column {
//        if (weather != null) {
//            Text(text = weather.timezone) // center
//            Text(text = weather.hourly[0].temp.toString()) // temperature
//            // big and bold
//            Text(text = "Feels like " + weather.hourly[0].feelsLike.toString())
//
//            LazyRow {
//                items(weather.hourly) { hourly ->
//                    hourlyWeatherColumn(hourly)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun hourlyWeatherColumn(hourly: HourlyForecast) {
//    Column {
//        Text(text = hourly.temp.toString())
//        Text(text = hourly.feelsLike.toString())
//        Image(
//            painter = rememberImagePainter("https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png"),
//            contentDescription = hourly.weather[0].description
//        )
//        Text(text = hourly.weather[0].description)
//    }
//}
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.weatherforecastapp.Data.DailyForecast
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WeatherUI(weather: WeatherResponse?) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        if (weather != null) {
            // Location header
            Text(
                text = weather.timezone,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            // Current temperature
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "${weather.hourly[0].temp.toInt()}°",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )

                // Weather icon
                Image(
                    painter = rememberImagePainter("https://openweathermap.org/img/wn/${weather.hourly[0].weather[0].icon}@4x.png"),
                    contentDescription = weather.hourly[0].weather[0].description,
                    modifier = Modifier.size(100.dp)
                )
            }

            // Feels like
            Text(
                text = "Feels like ${weather.hourly[0].feelsLike.toInt()}°",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Description
            Text(
                text = weather.hourly[0].weather[0].description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Hourly forecast
            Text(
                text = "Hourly Forecast",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(weather.hourly.take(24)) { hourly -> // Limit to 24 hours for better UX
                    HourlyForecastCard(hourly)
                }
            }

            // Daily forecast
            Text(
                text = "Daily Forecast",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ////////
            LazyColumn(
                //verticalArrangement = Arrangement.spacedBy(12.dp),
                //modifier = Modifier.padding(bottom = 16.dp)
            ) {
                items(weather.daily.take(5)) { daily -> // Limit to 24 hours for better UX
                    DailyForecast(daily)
                }
            }
        }
    }
}

@Composable
fun HourlyForecastCard(hourly: HourlyForecast) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(8.dp)
        ) {
            // Time (simplified - you might want to format this properly)
            Text(
                text = "${hourly.dt.toHour()}h",
                style = MaterialTheme.typography.labelMedium
            )

            // Weather icon
            Image(
                painter = rememberImagePainter("https://openweathermap.org/img/wn/${hourly.weather[0].icon}@2x.png"),
                contentDescription = hourly.weather[0].description,
                modifier = Modifier.size(40.dp)
            )

            // Temperature
            Text(
                text = "${hourly.temp.toInt()}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

///////////
//@Composable
////fun DailyForecast(daily: DailyForecast) {
////    Card(
////        modifier = Modifier
////            .fillMaxWidth()
////            .padding(vertical = 4.dp),
////        shape = RoundedCornerShape(12.dp),
////        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
////    ) {
////        Row(
////            modifier = Modifier
////                .padding(16.dp)
////                .fillMaxWidth(),
////            verticalAlignment = Alignment.CenterVertically,
////            horizontalArrangement = Arrangement.SpaceBetween
////        ) {
////            // Date column
////            Column(modifier = Modifier.weight(1f)) {
////                Text(
////                    text = daily.dt.toWeekday(), // You'll need to implement this
////                    style = MaterialTheme.typography.titleMedium,
////                    fontWeight = FontWeight.SemiBold
////                )
////                Text(
////                    text = daily.dt.toShortDate(), // You'll need to implement this
////                    style = MaterialTheme.typography.bodySmall,
////                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
////                )
////            }
////
////            // Weather icon
////            Image(
////                painter = rememberImagePainter("https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"),
////                contentDescription = daily.weather[0].description,
////                modifier = Modifier.size(40.dp)
////            )
////
////            // Temperature range
////            Column(
////                horizontalAlignment = Alignment.End,
////                modifier = Modifier.weight(1f)
////            ) {
////                Row(verticalAlignment = Alignment.CenterVertically) {
////                    Text(
////                        text = "H: ${daily.temp}°",
////                        style = MaterialTheme.typography.bodyLarge,
////                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
////                    )
////                    Spacer(modifier = Modifier.width(8.dp))
////                    Text(
////                        text = "L: ${daily.temp}°",
////                        style = MaterialTheme.typography.bodyMedium,
////                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
////                    )
////                }
////                Text(
////                    text = daily.weather[0].description.replaceFirstChar { it.uppercase() },
////                    style = MaterialTheme.typography.labelMedium,
////                    color = MaterialTheme.colorScheme.primary,
////                    modifier = Modifier.padding(top = 4.dp)
////                )
////            }
////        }
////    }
////}
@Composable
fun DailyForecast(daily: DailyForecast) {
    Card(
        modifier = Modifier
         //   .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceBetween // Fixes the spacing issue
        ) {
            // Date column
            Column(modifier = Modifier.wrapContentWidth()) {
                Text(
                    text = daily.dt.toWeekday(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = daily.dt.toShortDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Weather icon
            Image(
                painter = rememberImagePainter("https://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png"),
                contentDescription = daily.weather[0].description,
                modifier = Modifier.size(50.dp)
            )

            // Temperature range column
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.wrapContentWidth() // No extra space taken
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "H: ${daily.temp}°",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "L: ${daily.temp}°",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(
                    text = daily.weather[0].description.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}


// Add these extension functions for date formatting
fun Long.toWeekday(): String {
    val date = Date(this * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat("EEEE", Locale.getDefault())
    return formatter.format(date)
}

fun Long.toShortDate(): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat("MMM d", Locale.getDefault())
    return formatter.format(date)
}
// Extension function to convert timestamp to hour (simplified)
fun Long.toHour(): String {
    val date = Date(this * 1000)
    val formatter = SimpleDateFormat("h a", Locale.getDefault())
    return formatter.format(date)
}