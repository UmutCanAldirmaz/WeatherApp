package com.hopecoding.weatherapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hopecoding.weatherapp.data.model.ForecastResponse
import com.hopecoding.weatherapp.data.model.WeatherResponse
import com.hopecoding.weatherapp.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {
    private val _currentData = MutableLiveData<WeatherResponse>()
    private val _forecastData = MutableLiveData<ForecastResponse>()
    val forecastData: LiveData<ForecastResponse> = _forecastData
    val currentData: LiveData<WeatherResponse> = _currentData
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchForecast(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getForecastData(latitude, longitude, apiKey)
                _forecastData.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchCurrentWeather(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getCurrentWeather(latitude, longitude, apiKey)
                Log.d("WeatherViewModel", "Response: $response")
                _currentData.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
} 