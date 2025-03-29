
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.isLiveLiteralsEnabled
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.example.weatherforecastapp.Data.WeatherResponse
import com.example.weatherforecastapp.MainScreen
//import com.example.weatherforecastapp.favourites.model.FavouritesDAO
//import com.example.weatherforecastapp.favourites.model.FavouritesDAOImpl
//import com.example.weatherforecastapp.favourites.model.FavouritesRepo
//import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel
//import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModelFactory
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
fun setNavHost(application: Application,context: Context,activity:Activity) {
    val navController = rememberNavController()

    val currentScreenRoute = remember { mutableStateOf<ScreenRoute>(ScreenRoute.StartScreen) }


    Scaffold(

        bottomBar = {
            if (currentScreenRoute.value!=ScreenRoute.StartScreen){
                BottomNavBar(navController)
            }
        }
//        bottomBar = {
//            if (showBottomBar) BottomNavBar(navController)
//        }
    ) {paddingVals->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.StartScreen,
            modifier = Modifier.padding(paddingVals)

        ) {
            composable<ScreenRoute.StartScreen> {
                val repo = remember { StartScreenRepo(context) }
                val viewModel: StartScreeViewModel = viewModel(
                    factory = StartScreenViewModelFactory(repo)
                )

                val locrepo = remember { LocationsRepo() }
                val locviewModel: LocationsViewModel = viewModel(
                    factory = LocationsViewModelFactory(locrepo)
                )

                currentScreenRoute.value = ScreenRoute.StartScreen

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
                    },viewModel,activity,application,locviewModel
                )
            }

            composable<ScreenRoute.WeatherScreen> { backStackEntry ->
                val weatherScreen = backStackEntry.toRoute<ScreenRoute.WeatherScreen>()

                currentScreenRoute.value = ScreenRoute.WeatherScreen("")

                WeatherUI(weatherScreen.weather)
            }

            composable<ScreenRoute.LocationScreen> {backStackEntry->
                val weatherScreen = backStackEntry.toRoute<ScreenRoute.LocationScreen>()

                val repo = remember { LocationsRepo() }
                val viewModel: LocationsViewModel = viewModel(
                    factory = LocationsViewModelFactory(repo)
                )

                currentScreenRoute.value = ScreenRoute.LocationScreen("")

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

//                val dao = FavouritesDAOImpl.getInstance(application)
//                val repo = remember { FavouritesRepo(dao.getFavouritesDAO()) }
//                val viewModel: FavouritesViewModel = viewModel(
//                    factory = FavouritesViewModelFactory(repo)
//                )
//
//                FavScreenUI(
//                    goToLocationsForFavourites = {
//                        navController.navigate(ScreenRoute.LocationScreen(""))
//                    },
//                    viewModel = viewModel
//                )
            }

            composable<ScreenRoute.NotificationsScreen> {

            }
        }
    }

}

