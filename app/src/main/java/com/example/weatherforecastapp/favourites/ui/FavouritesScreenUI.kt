import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherforecastapp.favourites.viewmodel.FavouritesViewModel

@Composable
fun FavScreenUI(goToLocationsForFavourites:()->Unit,viewModel:FavouritesViewModel){

//    var favourites = viewModel.allFavourites //i have a feeling this will change
    val favourites by viewModel.allFavourites.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                goToLocationsForFavourites()
            }){
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {

            if (favourites==null){
                //show that its empty

            }
            else{
                //load the available favourites
                //iterator
               // favourites[0].city.name
                //this should be in a card
//                favourites[0].weather.hourly //take the function in the weather ui file
//                favourites[0].weather.daily //take the fun in weather ui file
//                no, these will be on a new screen, the weather ui screen

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
