import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.example.weatherforecastapp.favourites.model.FavClass
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel

@Composable
fun FavScreenUI(
    goToLocationsForFavourites: (Boolean) -> Unit,
    viewModel: FavouritesViewModel,
    goToWeather: (Double, Double) -> Unit
) {
    val favourites by viewModel.allFavourites.collectAsState(initial = emptyList())

    LaunchedEffect(favourites) {
        Log.d("Favourites", "Updated: $favourites")
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                goToLocationsForFavourites(true)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (favourites.isEmpty()) {
                Text(
                    text = "No favourites yet, add some?",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favourites) { favItem ->
                        FavItemCard(favItem,goToWeather,viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun FavItemCard(fav: FavClass,goToWeather:(Double,Double)->Unit, viewModel: FavouritesViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                goToWeather(fav.lat,fav.lon)
            },
        elevation = CardDefaults.elevatedCardElevation(4.dp) //
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = fav.city,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Lat: ${fav.lat}, Lon: ${fav.lon}")
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { viewModel.deleteFavourite(fav) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

//according to Saad, we will specify a flag in the ui function: LocationsUI.
//this will tell us if i came from favourites
//and this will be passed through the implementation of the lambda which will take the outer flag
//in the nav host, this method will be implemented to pass the outer flag to locationsUI
//and inside locations ui we handle the logic
//simple click on one of our favourites will lead to using the fetch weather in weather vm
//we may think of using shared prefs instead of room.
