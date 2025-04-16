package com.hopecoding.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherCurrentResponse(
    @SerializedName("main") val main: CurrentMain,
    @SerializedName("name") val name: String,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("dt") val dt: Long
)

data class CurrentMain(
    @SerializedName("temp") val temp: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("humidity") val humidity: Int
)

data class Sys(
    @SerializedName("country") val country: String
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)