package com.hopecoding.weatherapp.domain.repository

import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastResponse

interface APIRepository {
    suspend fun getForecastData(lat: Double?, lon: Double?, apiKey: String): WeatherForecastResponse
    suspend fun getCurrentWeather(lat: Double?, lon: Double?, apiKey: String): WeatherCurrentResponse
}