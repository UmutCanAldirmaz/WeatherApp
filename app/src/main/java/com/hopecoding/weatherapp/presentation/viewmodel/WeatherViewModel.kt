package com.hopecoding.weatherapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopecoding.weatherapp.data.location.LocationProvider
import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.data.model.WeatherForecastResponse
import com.hopecoding.weatherapp.domain.model.WeatherCard
import com.hopecoding.weatherapp.domain.repository.APIRepository
import com.hopecoding.weatherapp.util.convertTimestampToHour
import com.hopecoding.weatherapp.util.getDayOfYear
import com.hopecoding.weatherapp.util.getHourOfDay
import com.hopecoding.weatherapp.util.getTomorrowTimeRange
import com.hopecoding.weatherapp.util.toTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: APIRepository,
    private val locationProvider: LocationProvider
) : ViewModel() {

    private val apiKey = "eb293cb69baf127877adf77d28711842"

    private val _forecastData = MutableLiveData<WeatherForecastResponse?>()
    val forecastData: LiveData<WeatherForecastResponse?> = _forecastData

    private val _currentWeather = MutableLiveData<WeatherCurrentResponse?>()
    val currentWeather: LiveData<WeatherCurrentResponse?> = _currentWeather

    private val _weatherCards = MutableLiveData<List<WeatherCard>?>()
    val weatherCards: LiveData<List<WeatherCard>?> = _weatherCards

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error



    private var selectedTab = 0

    fun checkAndFetchWeather() {
        viewModelScope.launch {
            try {
                val (lat, lon) = if (locationProvider.hasLocationPermission() && locationProvider.isLocationEnabled()) {
                    val location = locationProvider.getCurrentLocation()
                    if (location != null) {
                        Pair(location.latitude, location.longitude)
                    } else {
                        locationProvider.getDefaultLocation()
                    }
                } else {
                    locationProvider.getDefaultLocation()
                }

                // İki çağrıyı paralel olarak yap ve tamamlanmasını bekle
                val forecastJob = launch { fetchForecast(lat, lon, apiKey) }
                val currentWeatherJob = launch { fetchCurrentWeather(lat, lon, apiKey) }
                forecastJob.join()
                currentWeatherJob.join()

            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred while fetching location"
            }
        }
    }

    private fun fetchForecast(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getForecastData(lat, lon, apiKey)
                Log.d("WeatherViewModel", "fetchForecast: response.list = ${response.list.size}")
                _forecastData.value = response
                updateWeatherCards(response)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred while fetching forecast"
            }
        }
    }

    private fun fetchCurrentWeather(lat: Double, lon: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCurrentWeather(lat, lon, apiKey)
                _currentWeather.value = response
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred while fetching current weather"
            }
        }
    }

    fun setSelectedTab(tab: Int) {
        selectedTab = tab
        if (_forecastData.value != null && _forecastData.value?.list?.isNotEmpty() == true) {
            updateWeatherCards(_forecastData.value)
        } else {
            Log.d("WeatherViewModel", "setSelectedTab: forecastData is null or loading")
        }
    }

    private fun updateWeatherCards(forecastData: WeatherForecastResponse?) {
        if (forecastData == null) {
            Log.d("WeatherViewModel", "updateWeatherCards: forecastData is null")
            _weatherCards.value = emptyList()
            return
        }

        val forecasts = when (selectedTab) {
            0 -> forecastData.list.take(4)
            1 -> getTomorrowForecasts(forecastData)
            2 -> getFiveDayForecasts(forecastData)
            else -> forecastData.list.take(4)
        }

        val cards = forecasts.mapIndexed { index, forecast ->
            WeatherCard(
                time = convertTimestampToHour(forecast.dateTime.toTimestamp() ?: 0L),
                temperature = "${forecast.main.temp.toInt()}°C",
                iconCode = forecast.weather?.firstOrNull()?.icon ?: "01d",
                isSelected = index == 1
            )
        }
        Log.d("WeatherViewModel", "updateWeatherCards: cards size = ${cards.size}, cards = $cards")
        _weatherCards.value = cards
    }

    private fun getTomorrowForecasts(forecastData: WeatherForecastResponse): List<WeatherForecastItem> {
        val (start, end) = getTomorrowTimeRange()
        return forecastData.list.filter {
            val timestamp = it.dateTime.toTimestamp() ?: 0L
            timestamp >= start && timestamp < end
        }.take(4)
    }

    private fun getFiveDayForecasts(forecastData: WeatherForecastResponse): List<WeatherForecastItem> {
        return forecastData.list.groupBy {
            it.dateTime.getDayOfYear()
        }.values.take(5).map { dayForecasts ->
            dayForecasts.firstOrNull { forecast ->
                forecast.dateTime.getHourOfDay() == 12
            } ?: dayForecasts.first()
        }
    }

    fun getSelectedForecast(position: Int): WeatherForecastItem? {
        return _forecastData.value?.let { forecastData ->
            val forecasts = when (selectedTab) {
                0 -> forecastData.list.take(4)
                1 -> getTomorrowForecasts(forecastData)
                2 -> getFiveDayForecasts(forecastData)
                else -> forecastData.list.take(4)
            }
            forecasts.getOrNull(position)
        }
    }
}