package com.hopecoding.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherForecastResponse(
    @SerializedName("list") val list: List<WeatherForecastItem>,
    @SerializedName("city") val city: City
)

data class WeatherForecastItem(
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<WeatherDetail>?,
    @SerializedName("dt_txt") val dateTime: String
)

data class Main(
    @SerializedName("temp") val temp: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("humidity") val humidity: Int
)

data class City(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)

data class WeatherDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)