package com.hopecoding.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main")
    val main: Main,
    @SerializedName("weather")
    val weather: List<Weather>,
    @SerializedName("name")
    val name: String,
    @SerializedName("sys")
    val sys: Sys
)

data class Main(
    @SerializedName("temp")
    val temperature: Double,
    @SerializedName("feels_like")
    val feelsLike: Double
)

data class Weather(
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
)

data class Sys(
    @SerializedName("sunset")
    val sunset: Long
) 