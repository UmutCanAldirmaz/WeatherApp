package com.hopecoding.weatherapp.domain.model

data class WeatherCard(
    val time: String,
    val temperature: String,
    val iconCode: String,
    var isSelected: Boolean = false
) 