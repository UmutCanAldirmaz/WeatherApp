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
        IconDescription("01d", "Açık Hava (Gündüz)"),
        IconDescription("01n", "Açık Hava (Gece)"),
        IconDescription("02d", "Az Bulutlu Hava (Gündüz)"),
        IconDescription("02n", "Az Bulutlu Hava (Gece)"),
        IconDescription("03d", "Dağınık Bulutlu Hava(Gündüz)"),
        IconDescription("03n", "Dağınık Bulutlu Hava (Gece)"),
        IconDescription("04d", "Kapalı Bulutlu Hava(Gündüz)"),
        IconDescription("04n", "Kapalı Bulutlu Hava (Gece)"),
        IconDescription("09d", "Çiseleme Hava (Gündüz)"),
        IconDescription("09n", "Çiseleme Hava (Gece)"),
        IconDescription("10d", "Yağmurlu Hava(Gündüz)"),
        IconDescription("10n", "Yağmurlu Hava (Gece)"),
        IconDescription("11d", "Gök gürültülü Fırtınalı Hava (Gündüz)"),
        IconDescription("11n", "Gökgürültülü Fırtınalı Hava (Gece)"),
        IconDescription("13d", "Karlı Hava (Gündüz)"),
        IconDescription("13n", "Karlı Hava (Gece)"),
        IconDescription("50d", "Sisli Hava (Gündüz)"),
        IconDescription("50n", "Sisli Hava (Gece)")
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
}