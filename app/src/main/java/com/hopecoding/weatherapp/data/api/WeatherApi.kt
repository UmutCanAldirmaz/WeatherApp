package com.hopecoding.weatherapp.data.api

import com.hopecoding.weatherapp.data.model.ForecastResponse
import com.hopecoding.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/forecast")
    suspend fun getForecastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
    ): ForecastResponse

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
    ): WeatherResponse
} 