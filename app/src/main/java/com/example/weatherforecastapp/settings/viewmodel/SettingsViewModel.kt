import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.weatherforecastapp.settings.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState

    fun updateLocationOption(context: Context, option: LocationOption) {
        _settingsState.value = _settingsState.value.copy(locationOption = option)
        SettingsManager.setLocationOption(context, option)
    }

    fun updateTemperatureUnit(context: Context, unit: TemperatureUnit) {
        _settingsState.value = _settingsState.value.copy(temperatureUnit = unit)
        SettingsManager.setTemperatureUnit(context, unit)
    }

    fun updateWindSpeedUnit(context: Context, unit: WindSpeedUnit) {
        _settingsState.value = _settingsState.value.copy(windSpeedUnit = unit)
        SettingsManager.setWindSpeedUnit(context, unit)
    }

    fun updateLanguage(context: Context, language: Language) {
        _settingsState.value = _settingsState.value.copy(language = language)
        SettingsManager.setLanguage(context, language)
        SettingsManager.setLocale(context, language) // Apply the language change
    }
}
