package com.hopecoding.weatherapp.domain.model

data class WeatherCard(
    val time: String,
    val date:String,
    val temperature: String,
    val iconCode: String,
    var isSelected: Boolean = false
) 