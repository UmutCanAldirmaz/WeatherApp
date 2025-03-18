package com.hopecoding.weatherapp.data.repository

import com.hopecoding.weatherapp.data.api.WeatherApi
import com.hopecoding.weatherapp.data.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {
    suspend fun getCurrentWeather(city: String, apiKey: String): WeatherResponse {
        return api.getCurrentWeather(city, apiKey)
    }
} 