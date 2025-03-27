import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun BottomNavBar(navController: NavController, currentRoute: ScreenRoute?) {
    NavigationBar {
        ScreenRoute.bottomNavItems.forEach { screen ->
            NavigationBarItem(
                icon = { screen.icon?.let { Icon(it, contentDescription = screen.title) } },
                label = { Text(screen.title) },
                selected = screen == currentRoute,
                onClick = {
                    if (screen != currentRoute) {
                        val route = when (screen) {
                            is ScreenRoute.WeatherScreen -> ScreenRoute.WeatherScreen("").toString()
                            else -> screen::class.simpleName ?: ""
                        }
                        navController.navigate(route)
                        navController.navigate(ScreenRoute.LocationScreen)

                    }
                }
            )
        }
    }
}

