
import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.MainScreen
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsActivity
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsUI
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreenViewModelFactory
import com.google.gson.Gson
import kotlinx.serialization.json.Json

//@Composable
//fun setNavHost() {
//    var navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = ScreenRoute.StartScreen //StartDestination should not be a simple class name reference.
//
//    ){
//        composable<ScreenRoute.StartScreen>{ // there is NO issue here, it was a missing dependency
//            MainScreen(
//                goToLocationOrWeather = {isLocationsEnabled ->
//                   // if (isLocationsEnabled) navController.navigate(ScreenRoute.WeatherScreen(Json.encodeToString(WeatherResponse(/* your weather data which will be recieved from requesting location permission*/))).weatherJson)
//                    //but first we should request location in the main screen
//                    //else
//                        navController.navigate(ScreenRoute.LocationScreen)
//                }
//            )
//        }
//
////        composable<ScreenRoute.WeatherScreen> {
////            var profile = it.toRoute<ScreenRoute.WeatherScreen>()
////            WeatherUI(profile.weather)
////
////        }
//        composable(
//            route = ScreenRoute.WeatherScreen::class.java.name + "/{weatherJson}",
//            arguments = listOf(navArgument("weatherJson") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val weatherJson = backStackEntry.arguments?.getString("weatherJson")!!
//            val weather = Json.decodeFromString<WeatherResponse>(weatherJson)
//            WeatherUI(weather)
//        }
//
//        composable<ScreenRoute.LocationScreen> {
//
//
//            val repo = remember { LocationsRepo() }
//            val viewModel: LocationsViewModel = viewModel(
//                factory = LocationsViewModelFactory(repo)
//            )
//
//            var profile = it.toRoute<ScreenRoute.LocationScreen>()
//
//            LocationsUI(viewModel,
//                goToWeather = {current/*,hourly,daily*/ ->
//                    navController.navigate(ScreenRoute.WeatherScreen(Json.encodeToString(current)))
//                    Log.i("WEATHER", "goToWeather:currently: $current")
//                }
//            )
//        }
//
//        composable<ScreenRoute.FavouritesScreen> {
//        }
//
//        composable<ScreenRoute.NotificationsScreen> {
//
//        }
//    }
//}

@Composable
fun setNavHost(application: Application) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.StartScreen
    ) {
        composable<ScreenRoute.StartScreen> {
            val repo = remember { StartScreenRepo() }
            val viewModel: StartScreeViewModel = viewModel(
                factory = StartScreenViewModelFactory(repo)
            )

            val locrepo = remember { LocationsRepo() }
            val locviewModel: LocationsViewModel = viewModel(
                factory = LocationsViewModelFactory(locrepo)
            )

            MainScreen(
                goToLocationOrWeather = { isLocationsEnabled,weatherResp ->
                    if (isLocationsEnabled){
                        if (weatherResp==null){
                            Log.e("weather", "start to weatherscreen: response is $weatherResp", )
                        }
                       else{
                            navController.navigate(ScreenRoute.WeatherScreen(Gson().toJson(weatherResp)))
                            Log.i("weather", "start to weatherscreen: response is $weatherResp", )

                        }
                    }
                    else
                        navController.navigate(ScreenRoute.LocationScreen)
                },viewModel,application,locviewModel
            )
        }

        composable<ScreenRoute.WeatherScreen> { backStackEntry ->
            val weatherScreen = backStackEntry.toRoute<ScreenRoute.WeatherScreen>()
            WeatherUI(weatherScreen.weather)
        }

        composable<ScreenRoute.LocationScreen> {
            val repo = remember { LocationsRepo() }
            val viewModel: LocationsViewModel = viewModel(
                factory = LocationsViewModelFactory(repo)
            )
            LocationsUI(
                viewModel = viewModel,
                goToWeather = { current ->
                    val gson = Gson()
                    navController.navigate(ScreenRoute.WeatherScreen(gson.toJson(current)))
                }
            )
        }

        composable<ScreenRoute.FavouritesScreen> {

        }

        composable<ScreenRoute.NotificationsScreen> {

        }
    }
}