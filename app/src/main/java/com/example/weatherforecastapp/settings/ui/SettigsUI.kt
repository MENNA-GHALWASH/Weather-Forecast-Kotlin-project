package com.example.weatherforecastapp.settings.ui


import SettingsViewModel
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.weatherforecastapp.settings.model.Language
import com.example.weatherforecastapp.settings.model.LocationOption
import com.example.weatherforecastapp.settings.model.TemperatureUnit
import com.example.weatherforecastapp.settings.model.WindSpeedUnit

@Composable
fun SettingsUI(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val settingsState by viewModel.settingsState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LocationOptionPicker(
            selectedOption = settingsState.locationOption,
            onOptionSelected = { viewModel.updateLocationOption(context, it) }
        )

        TemperatureUnitPicker(
            selectedUnit = settingsState.temperatureUnit,
            onUnitSelected = { viewModel.updateTemperatureUnit(context, it) }
        )

        WindSpeedUnitPicker(
            selectedUnit = settingsState.windSpeedUnit,
            onUnitSelected = { viewModel.updateWindSpeedUnit(context, it) }
        )

        LanguagePicker(
            selectedLanguage = settingsState.language,
            onLanguageSelected = { viewModel.updateLanguage(context, it) }
        )
    }
}


@Composable
fun LocationOptionPicker(selectedOption: LocationOption, onOptionSelected: (LocationOption) -> Unit) {
    Column {
        Text(text = "Location Option")
        Row {
            RadioButton(
                selected = selectedOption == LocationOption.GPS,
                onClick = { onOptionSelected(LocationOption.GPS) }
            )
            Text(text = "GPS")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedOption == LocationOption.MAP,
                onClick = { onOptionSelected(LocationOption.MAP) }
            )
            Text(text = "Map")
        }
    }
}

@Composable
fun TemperatureUnitPicker(selectedUnit: TemperatureUnit, onUnitSelected: (TemperatureUnit) -> Unit) {
    Column {
        Text(text = "Temperature Unit")
        Row {
            RadioButton(
                selected = selectedUnit == TemperatureUnit.Kelvin,
                onClick = { onUnitSelected(TemperatureUnit.Kelvin) }
            )
            Text(text = "Kelvin")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedUnit == TemperatureUnit.Celsius,
                onClick = { onUnitSelected(TemperatureUnit.Celsius) }
            )
            Text(text = "Celsius")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedUnit == TemperatureUnit.Fahrenheit,
                onClick = { onUnitSelected(TemperatureUnit.Fahrenheit) }
            )
            Text(text = "Fahrenheit")
        }
    }
}

@Composable
fun WindSpeedUnitPicker(selectedUnit: WindSpeedUnit, onUnitSelected: (WindSpeedUnit) -> Unit) {
    Column {
        Text(text = "Wind Speed Unit")
        Row {
            RadioButton(
                selected = selectedUnit == WindSpeedUnit.MeterPerSecond,
                onClick = { onUnitSelected(WindSpeedUnit.MeterPerSecond) }
            )
            Text(text = "Meter/sec")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedUnit == WindSpeedUnit.MilesPerHour,
                onClick = { onUnitSelected(WindSpeedUnit.MilesPerHour) }
            )
            Text(text = "Miles/hour")
        }
    }
}

@Composable
fun LanguagePicker(selectedLanguage: Language, onLanguageSelected: (Language) -> Unit) {
    Column {
        Text(text = "Language")
        Row {
            RadioButton(
                selected = selectedLanguage == Language.English,
                onClick = { onLanguageSelected(Language.English) }
            )
            Text(text = "English")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedLanguage == Language.Arabic,
                onClick = { onLanguageSelected(Language.Arabic) }
            )
            Text(text = "Arabic")
        }
    }
}