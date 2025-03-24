package com.hopecoding.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list") val forecastList: List<ForecastItem>,
    @SerializedName("city") val city: City
)

data class City(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)

data class ForecastItem(
    @SerializedName("dt") val time: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>
)

data class Main(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("humidity") val humidity: Int
)

data class Weather(
    @SerializedName("main") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)