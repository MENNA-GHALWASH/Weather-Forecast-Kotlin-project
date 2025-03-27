
import android.location.Location
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherforecastapp.Data.HourlyForecast
import com.example.weatherforecastapp.Data.WeatherResponse
import com.google.gson.Gson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
sealed class ScreenRoute(){

    //when we add parameters here we do it to pass data from an old screen to the new one
    //so if i want to identify from where i came, i can add a parameter called source here
    //then add the same parameter to the ui , that is this
    //first the parameter is passedm to the ui method
    @Serializable
    object StartScreen: ScreenRoute()

    //so location screen wont take anydata
    @Serializable
    data class LocationScreen(val source:String): ScreenRoute()

    @Serializable
    data class WeatherScreen(val weatherJson: String) : ScreenRoute() {
        val weather: WeatherResponse
            get() = Gson().fromJson(weatherJson, WeatherResponse::class.java)
    }

    @Serializable
    //data class NotificationsScreen(var loc: SerializableLocation? = null):ScreenRoute()
    object NotificationsScreen: ScreenRoute()

    @Serializable
    object FavouritesScreen: ScreenRoute()

    @Serializable
    object SettingsScreen: ScreenRoute()



    val icon: ImageVector?
        get() = when(this) {
            is WeatherScreen -> Icons.Default.Home
            is FavouritesScreen -> Icons.Default.Star
            is NotificationsScreen -> Icons.Default.Notifications
            is SettingsScreen -> Icons.Default.Settings
            is LocationScreen -> Icons.Default.LocationOn
            else -> null
        }

    val title: String
        get() = when(this) {
            is WeatherScreen -> "Weather"
            is LocationScreen -> "Select Location"
            is NotificationsScreen -> "Notifications"
            is FavouritesScreen -> "Favorites"
            is SettingsScreen -> "Settings"
            else -> ""
        }

    companion object {
        val bottomNavItems = listOf(
            WeatherScreen(""), // Dummy instance for navigation , will this lead to problems?
            FavouritesScreen,
            NotificationsScreen,
            LocationScreen,
            SettingsScreen
        )
    }
}

//the user clicks on the start btn
//if the user already has location stored within the app then it wont ask for it
//if it's the user's first time, then he will get the notification asking for location services
//if he accepts it then he gets directed to the main screen that shows the weather for his location
//if he denies it, he will have to select a location from the map, or the autocomplete textview
//once he selects it he is directed to the same screen for the weather
//the weather screen will have detailed description for hourly weather on that day
//at the bottom of the screen , we will have the forecast for the next 5 days
//this forecast will have the temperatures: high and low, and precipitation likelihood

//at the bottom of that weather screen , we will have a nav bar with an add icon to add a favourite location
//the nav bar will also have a notifications bell which will direct to us the notifications screen
//in the notifications screen you get to pick locations for the place you want to know the weather
//in the notifications screen, you can choose the date and time for receiving the notification

