package com.hopecoding.weatherapp.domain.usecase

import com.hopecoding.weatherapp.data.model.WeatherResponse
import com.hopecoding.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double, apiKey: String): WeatherResponse {
        return repository.getCurrentWeather(lat, lon, apiKey)
    }
} 