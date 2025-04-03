
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.weatherforecastapp.MainScreen
import com.example.weatherforecastapp.favourites.model.FavClass
import com.example.weatherforecastapp.favourites.model.FavouritesDAOImpl
import com.example.weatherforecastapp.favourites.model.FavouritesLocalDataSource
import com.example.weatherforecastapp.favourites.model.FavouritesRepo
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModelFactory
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherDao
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherDatabase
import com.example.weatherforecastapp.home.daily_forecast.model.WeatherRepo
import com.example.weatherforecastapp.home.daily_forecast.viewmodel.WeatherViewModel
import com.example.weatherforecastapp.home.daily_forecast.viewmodel.WeatherViewModelFactory
import com.example.weatherforecastapp.notifications_and_Alerts.ui.WeatherAlertsUI
import com.example.weatherforecastapp.repository.WeatherAlertRepository
//import com.example.weatherforecastapp.favourites.model.FavouritesDAO
//import com.example.weatherforecastapp.favourites.model.FavouritesDAOImpl
//import com.example.weatherforecastapp.favourites.model.FavouritesRepo
//import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel
//import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModelFactory
import com.example.weatherforecastapp.selecting_location.get_location_with_map.model.LocationsRepo
import com.example.weatherforecastapp.selecting_location.get_location_with_map.ui.LocationsUI
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModel
import com.example.weatherforecastapp.selecting_location.get_location_with_map.viewmodel.LocationsViewModelFactory
import com.example.weatherforecastapp.settings.ui.SettingsUI
import com.example.weatherforecastapp.start_screen.model.StartScreenRepo
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreeViewModel
import com.example.weatherforecastapp.start_screen.viewmodel.StartScreenViewModelFactory
import com.example.weatherforecastapp.viewmodel.WeatherAlertViewModel
import com.google.gson.Gson

@RequiresApi(Build.VERSION_CODES.O)
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

    ) {paddingVals->
        NavHost(
            navController = navController,
            startDestination = ScreenRoute.StartScreen,
            modifier = Modifier.padding(paddingVals)

        ) {
            composable<ScreenRoute.StartScreen> {
                val repo = remember { StartScreenRepo(context,activity) }
                val viewModel: StartScreeViewModel = viewModel(
                    factory = StartScreenViewModelFactory(repo)
                )

                var dao = WeatherDatabase.getDatabase(context).weatherDao()
                val locrepo = remember { WeatherRepo(dao,context) }
                val locviewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModelFactory(locrepo)
                )

                currentScreenRoute.value = ScreenRoute.StartScreen

                MainScreen(
                    goToLocationOrWeather = { isLocationsEnabled,weatherResp,cityname ->
                        if (isLocationsEnabled){
                            if (weatherResp==null){
                                Log.e("weather", "start to weatherscreen: response is $weatherResp", )
                            }
                            else{
                                navController.navigate(ScreenRoute.WeatherScreen(weatherResp.lat,weatherResp.lon,cityname))
                            }
                        }
                        else
                            navController.navigate(ScreenRoute.LocationScreen())
                    },viewModel,activity,application,locviewModel,context
                )
            }

            composable<ScreenRoute.WeatherScreen> { backStackEntry ->
                val weatherScreen = backStackEntry.toRoute<ScreenRoute.WeatherScreen>()
                //extracts items from weatherscreen data class
                currentScreenRoute.value = ScreenRoute.WeatherScreen(weatherScreen.lat,weatherScreen.lon,weatherScreen.city)

                var dao = WeatherDatabase.getDatabase(context).weatherDao()

                val repo = remember { WeatherRepo(dao,context) }
                val viewModel: WeatherViewModel = viewModel(
                    factory = WeatherViewModelFactory(repo)
                )

                WeatherUI(weatherScreen.lat,weatherScreen.lon,weatherScreen.city,viewModel)
            }

            composable<ScreenRoute.LocationScreen> {backStackEntry->

                val locationScreen = backStackEntry.toRoute<ScreenRoute.LocationScreen>()
                val flag = locationScreen.fromFav //this gets the data from our data class


                val repo = remember { LocationsRepo() }
                val viewModel: LocationsViewModel = viewModel(
                    factory = LocationsViewModelFactory(repo)
                )

                val dao = FavouritesDAOImpl.getInstance(application)
                val lds =  FavouritesLocalDataSource(dao.getFavouritesDAO())

                val favrepo = remember { FavouritesRepo(lds) }
                val favviewModel: FavouritesViewModel = viewModel(
                    factory = FavouritesViewModelFactory(favrepo)
                )
//

                currentScreenRoute.value = ScreenRoute.LocationScreen()

                val Favrepo = remember { FavouritesRepo(lds) }
                val Favviewmodel: FavouritesViewModel = viewModel(
                    factory = FavouritesViewModelFactory(Favrepo)
                )

                //get rid of flag param in loc ui
                LocationsUI(
                    viewModel = viewModel,
                    goToWeather = { lat,lon,city ->
                       // val gson = Gson()

                        if(!flag){ //not from fav screen
                            navController.navigate(ScreenRoute.WeatherScreen(lat,lon,city))
                        }
                        else{

                                favviewModel.addToFavourites(FavClass(lat,lon, city))
                                Log.i("Fav", "Added to favourites: ${FavClass(lat,lon, city)}")

                            navController.navigate(ScreenRoute.FavouritesScreen(null,city))
                            //pop the back stack and send the data back to favourites
                            //pass data to favourites or simply observe using flow
                            //navController.navigate(ScreenRoute.FavouritesScreen) //more to do here
                        }
                    },favviewModel
                )
            }

            composable<ScreenRoute.FavouritesScreen> { backStackEntry ->
                val fromfav = backStackEntry.toRoute<ScreenRoute.FavouritesScreen>()

                val city = fromfav.city
                val weather = fromfav.weather?:null

                val dao = FavouritesDAOImpl.getInstance(application)
                val lds =  FavouritesLocalDataSource(dao.getFavouritesDAO())

                val repo = remember { FavouritesRepo(lds) }
                val viewModel: FavouritesViewModel = viewModel(
                    factory = FavouritesViewModelFactory(repo)
                )

                if (weather != null) {
                    viewModel.addToFavourites(FavClass(weather.lat, weather.lon, city))
                    Log.i("Fav", "setNavHost: added to favourites: ${FavClass(weather.lat, weather.lon, city)}")
                }

                FavScreenUI(
                    goToLocationsForFavourites = { flag ->
                        navController.navigate(ScreenRoute.LocationScreen(flag))
                    },
                    viewModel = viewModel,
                    goToWeather = {lat,lon,name ->
                        navController.navigate(ScreenRoute.WeatherScreen(lat, lon, name)) // i am not sure
                    }
                )
            }

            composable<ScreenRoute.NotificationsScreen> {

                val repo = WeatherAlertRepository()
                val vm = WeatherAlertViewModel(repo)

                val locrepo = LocationsRepo()
                val locviewmodel: LocationsViewModel = viewModel(
                    factory = LocationsViewModelFactory(locrepo)
                )
//                repo
//                factory
                WeatherAlertsUI(vm, locviewmodel
//                   factory
                )
            }

            composable<ScreenRoute.SettingsScreen>{

               // repo
               // factory
                val vm = SettingsViewModel()
                SettingsUI( vm
                 //  factory
                )
            }
        }
    }

}

//weather needs to constantly update: so place weather calls inside the weather view model
//i also need to cache my current location and pass it on the nav bar navigation
