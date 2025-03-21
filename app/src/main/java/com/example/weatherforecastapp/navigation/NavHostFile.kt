
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecastapp.MainScreen
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsUI

@Composable
fun setNavHost() {
    var navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.StartScreen //StartDestination should not be a simple class name reference.

    ){
        composable<ScreenRoute.StartScreen>{ // there is NO issue here, it was a missing dependency
            MainScreen(
                goToLocationOrWeather = {isLocationsEnabled ->
                    if (isLocationsEnabled) navController.navigate(ScreenRoute.WeatherScreen)
                    else navController.navigate(ScreenRoute.LocationScreen)

                }
            )
        }

        composable<ScreenRoute.WeatherScreen> {
            var profile = it.toRoute<ScreenRoute.LocationScreen>()
            //LocationsUI()
        }

        composable<ScreenRoute.LocationScreen> {
            LocationsUI()
        }

        composable<ScreenRoute.FavouritesScreen> {
        }

        composable<ScreenRoute.NotificationsScreen> {

        }
    }
}