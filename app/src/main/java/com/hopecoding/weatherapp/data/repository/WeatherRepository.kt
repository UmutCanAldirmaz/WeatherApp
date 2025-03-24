package com.hopecoding.weatherapp.data.repository

import com.hopecoding.weatherapp.data.api.WeatherApi
import com.hopecoding.weatherapp.data.model.ForecastResponse
import com.hopecoding.weatherapp.data.model.WeatherResponse
import com.hopecoding.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getForecastData(
        lat: Double,
        lon: Double,
        apiKey: String
    ): ForecastResponse {
        return api.getForecastData(lat, lon, apiKey)
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): WeatherResponse {
        return api.getCurrentWeather(lat, lon, apiKey)
    }
} 