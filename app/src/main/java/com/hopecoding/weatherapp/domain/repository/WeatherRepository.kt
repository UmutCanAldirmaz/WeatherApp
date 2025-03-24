package com.hopecoding.weatherapp.domain.repository

import com.hopecoding.weatherapp.data.model.ForecastResponse
import com.hopecoding.weatherapp.data.model.WeatherResponse

interface WeatherRepository {
    suspend fun getForecastData(lat: Double, lon: Double, apiKey: String): ForecastResponse
    suspend fun getCurrentWeather(lat: Double, lon: Double, apiKey: String): WeatherResponse
} 