
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun setNavHost() {
    var navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.StartScreen //StartDestination should not be a simple class name reference.

    ){
        composable<ScreenRoute.WeatherScreen> {

          //  var profile = it.toRoute<ScreenRoute.Login>()

            /*LoginUI(
                goToHome = {email, password ->
                    navController.navigate(ScreenRoute.Home(email))
                },profile.username,profile.password // i am really not sure if this is correct
            )*/
        }

        composable<ScreenRoute.LocationScreen> {
           /* SignupUI(
                goToLogin = {username,password,confpsswd ->
                    if (confpsswd.equals(password)&&(!(username.equals(null)||username.equals("")))){
                        navController.navigate(ScreenRoute.Login(username = username, password = password))
                    }
                }
            )*/
        }

        composable<ScreenRoute.FavouritesScreen> {

        //    var profile = it.toRoute<ScreenRoute.Home>()

            /*HomeUI (
                goBackToStart = {
                    navController.popBackStack(ScreenRoute.SignUp,false)
                },profile.name
            )*/
        }

        composable<ScreenRoute.NotificationsScreen> {

        //    var profile = it.toRoute<ScreenRoute.Home>()

            /*HomeUI (
                goBackToStart = {
                    navController.popBackStack(ScreenRoute.SignUp,false)
                },profile.name
            )*/
        }
    }
}