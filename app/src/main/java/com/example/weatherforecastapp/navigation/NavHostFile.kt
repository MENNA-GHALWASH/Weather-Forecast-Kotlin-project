
import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.MainScreen
import com.example.weatherforecastapp.favourites.model.FavouritesDAO
import com.example.weatherforecastapp.favourites.model.FavouritesDAOImpl
import com.example.weatherforecastapp.favourites.model.FavouritesRepo
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModelFactory
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsActivity
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsUI
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreenViewModelFactory
import com.google.gson.Gson

@Composable
fun setNavHost(application: Application) {
    val navController = rememberNavController()

//    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
//        ScreenRoute.bottomNavItems.find { it::class.simpleName == route }
//    }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route?.let { route ->
        try {
            // Try to parse the route as one of our ScreenRoute objects
            when {
                route.startsWith("WeatherScreen") -> ScreenRoute.WeatherScreen("")
                route == "LocationScreen" -> ScreenRoute.LocationScreen("Start_screen")
                route == "FavouritesScreen" -> ScreenRoute.FavouritesScreen
                route == "NotificationsScreen" -> ScreenRoute.NotificationsScreen
                route == "SettingsScreen" -> ScreenRoute.SettingsScreen
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != null ) {
                BottomNavBar(navController, currentRoute)
            }
        }
    ) {paddingVals->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.StartScreen,
            modifier = Modifier.padding(paddingVals)

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
                            navController.navigate(ScreenRoute.LocationScreen("Start_screen"))
                    },viewModel,application,locviewModel
                )
            }

            composable<ScreenRoute.WeatherScreen> { backStackEntry ->
                val weatherScreen = backStackEntry.toRoute<ScreenRoute.WeatherScreen>()
                WeatherUI(weatherScreen.weather)
            }

            composable<ScreenRoute.LocationScreen> {backStackEntry->
                val weatherScreen = backStackEntry.toRoute<ScreenRoute.LocationScreen>()

                val repo = remember { LocationsRepo() }
                val viewModel: LocationsViewModel = viewModel(
                    factory = LocationsViewModelFactory(repo)
                )

                //if weatherscreen.source == Start_screen -> do xyz
                //else do favourites

                LocationsUI(
                    viewModel = viewModel,
                    goToWeather = { current ->
                        if(weatherScreen.source=="Start_screen"){
                            val gson = Gson()
                            navController.navigate(ScreenRoute.WeatherScreen(gson.toJson(current)))
                        }
                        else{
                            //pop the back stack and send the data back to favourites
                            //pass data to favourites or simply observe using flow
                            //navController.navigate(ScreenRoute.FavouritesScreen) //more to do here
                        }
                    }
                )
            }

            composable<ScreenRoute.FavouritesScreen> {
                val dao = FavouritesDAOImpl.getInstance(application)
                val repo = remember { FavouritesRepo(dao.getFavouritesDAO()) }
                val viewModel: FavouritesViewModel = viewModel(
                    factory = FavouritesViewModelFactory(repo)
                )

                FavScreenUI(
                    goToLocationsForFavourites = {
                        navController.navigate(ScreenRoute.LocationScreen(""))
                    },
                    viewModel = viewModel
                )
            }

            composable<ScreenRoute.NotificationsScreen> {

            }
        }
    }

}