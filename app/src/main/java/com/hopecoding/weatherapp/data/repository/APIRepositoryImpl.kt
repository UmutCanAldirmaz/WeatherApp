package com.hopecoding.weatherapp.data.repository

import com.hopecoding.weatherapp.data.api.WeatherApiService
import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastResponse
import com.hopecoding.weatherapp.domain.repository.APIRepository
import javax.inject.Inject

class APIRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : APIRepository {
    override suspend fun getForecastData(
        lat: Double?,
        lon: Double?,
        apiKey: String,
    ): WeatherForecastResponse {
        return apiService.getForecastData(lat, lon, apiKey,"metric")
    }

    override suspend fun getCurrentWeather(
        lat: Double?,
        lon: Double?,
        apiKey: String,
    ): WeatherCurrentResponse {
        return apiService.getCurrentWeather(lat, lon, apiKey,"metric")
    }
}