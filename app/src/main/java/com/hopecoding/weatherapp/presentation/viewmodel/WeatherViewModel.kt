package com.hopecoding.weatherapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopecoding.weatherapp.BuildConfig
import com.hopecoding.weatherapp.data.location.LocationProvider
import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.data.model.WeatherForecastResponse
import com.hopecoding.weatherapp.domain.model.IconDescription
import com.hopecoding.weatherapp.domain.model.WeatherCard
import com.hopecoding.weatherapp.domain.repository.APIRepository
import com.hopecoding.weatherapp.util.getTodayForecasts
import com.hopecoding.weatherapp.util.getTomorrowForecasts
import com.hopecoding.weatherapp.util.toWeatherCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: APIRepository,
    val locationProvider: LocationProvider
) : ViewModel() {

    val apiKey = BuildConfig.API_KEY

    private val _forecastData = MutableLiveData<WeatherForecastResponse?>()
    val forecastData: LiveData<WeatherForecastResponse?> = _forecastData

    private val _currentWeather = MutableLiveData<WeatherCurrentResponse?>()
    val currentWeather: LiveData<WeatherCurrentResponse?> = _currentWeather

    private val _weatherCards = MutableLiveData<List<WeatherCard>?>()
    val weatherCards: LiveData<List<WeatherCard>?> = _weatherCards

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _showLocationPrompt = MutableLiveData<Boolean>()
    val showLocationPrompt: LiveData<Boolean> get() = _showLocationPrompt

    private val iconDescriptions = listOf(
        IconDescription("01d", "Clear Sky (Day)"),
        IconDescription("01n", "Clear Sky (Night)"),
        IconDescription("02d", "Few Clouds (Day)"),
        IconDescription("02n", "Few Clouds (Night)"),
        IconDescription("03d", "Scattered Clouds (Day)"),
        IconDescription("03n", "Scattered Clouds (Night)"),
        IconDescription("04d", "Overcast Clouds (Day)"),
        IconDescription("04n", "Overcast Clouds (Night)"),
        IconDescription("09d", "Drizzle (Day)"),
        IconDescription("09n", "Drizzle (Night)"),
        IconDescription("10d", "Rainy Weather (Day)"),
        IconDescription("10n", "Rainy Weather (Night)"),
        IconDescription("11d", "Thunderstorm (Day)"),
        IconDescription("11n", "Thunderstorm (Night)"),
        IconDescription("13d", "Snowy Weather (Day)"),
        IconDescription("13n", "Snowy Weather (Night)"),
        IconDescription("50d", "Foggy Weather (Day)"),
        IconDescription("50n", "Foggy Weather (Night)")
    )

    // Kullanılan ikonları döndüren fonksiyon
    fun getUsedIconDescriptions(): List<IconDescription> {
        val usedIconCodes = _weatherCards.value?.map { it.iconCode }?.distinct() ?: emptyList()
        return iconDescriptions.filter { it.iconCode in usedIconCodes }
    }

    fun resetData() {
        _forecastData.value = null
        _currentWeather.value = null
        _weatherCards.value = null
        _error.value = null

    }

    fun checkAndFetchWeather() {
        viewModelScope.launch {
            resetData() // Eski verileri temizle
            if (locationProvider.hasLocationPermission() && locationProvider.isLocationEnabled()) {
                Timber.d("checkAndFetchWeather: Location permission granted and location enabled")
                val location = locationProvider.getCurrentLocation()
                val lat = location?.latitude
                val lon = location?.longitude
                if (lat != null && lon != null) {
                    fetchCurrentWeather(lat, lon, apiKey)
                    fetchForecast(lat, lon, apiKey)
                    _showLocationPrompt.value = false
                } else {
                    _error.value = "Mevcut konum alınamadı.Konum Bilgisi Null Gelmektedir."
                }
            } else {
                _showLocationPrompt.value = true
            }
        }
    }





    fun fetchForecast(lat: Double?, lon: Double?, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getForecastData(lat, lon, apiKey)
                Timber.d("fetchForecast: response.list = %s", response.list.size)
                _forecastData.value = response
                updateWeatherCards(response)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Hava durumu tahmini alınamadı: ${e.message ?: "Bilinmeyen hata"}"
            }
        }
    }

    fun fetchCurrentWeather(lat: Double?, lon: Double?, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(lat, lon, apiKey)
                _currentWeather.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Mevcut hava durumu alınamadı: ${e.message ?: "Bilinmeyen hata"}"
            }
        }
    }

    fun setSelectedTab(tab: Int) {
        if (_forecastData.value != null && _selectedTab.value != tab) {
            _selectedTab.value = tab
            updateWeatherCards(_forecastData.value)
        } else {
            Timber.d("setSelectedTab: forecastData is null or same tab selected")
        }
    }

    private fun updateWeatherCards(forecastData: WeatherForecastResponse?) {
        if (forecastData == null) {
            Timber.d("updateWeatherCards: forecastData is null")
            _weatherCards.value = emptyList()
            return
        }

        val forecasts = when (_selectedTab.value) {
            0 -> forecastData.getTodayForecasts()
            1 -> forecastData.getTomorrowForecasts()
            2 -> forecastData.list
            else -> forecastData.getTodayForecasts()
        }

        val cards = forecasts.toWeatherCards()
        Timber.d("updateWeatherCards: cards size = ${cards.size}, cards = $cards")
        _weatherCards.value = cards
    }

    fun getSelectedForecast(position: Int): WeatherForecastItem? {
        return _forecastData.value?.let { forecastData ->
            val forecasts = when (_selectedTab.value) {
                0 -> forecastData.getTodayForecasts()
                1 -> forecastData.getTomorrowForecasts()
                2 -> forecastData.list
                else -> forecastData.getTodayForecasts()
            }
            forecasts.getOrNull(position)
        }
    }

    fun resetLocationPrompt() {
        _showLocationPrompt.value = false
    }
}