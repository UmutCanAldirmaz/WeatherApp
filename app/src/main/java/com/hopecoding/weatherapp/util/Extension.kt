package com.hopecoding.weatherapp.util

import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.data.model.WeatherForecastResponse
import com.hopecoding.weatherapp.domain.model.WeatherCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val sdfTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
private val sdfHour = SimpleDateFormat("HH:mm", Locale.getDefault())
private val sdfDate = SimpleDateFormat("dd MMM", Locale.getDefault())

fun convertTimestampToHour(timestamp: Long): String {
    return sdfHour.format(Date(timestamp * 1000))
}

fun convertTimestampToDate(timestamp: Long): String {
    return sdfDate.format(Date(timestamp * 1000))
}

fun String.toTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss"): Long? {
    return try {
        sdfTimestamp.parse(this)?.time?.div(1000)
    } catch (e: Exception) {
        null
    }
}

fun getTodayTimeRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    val start = calendar.timeInMillis / 1000
    val end = start + 24 * 60 * 60 // Bugün gece yarısına kadar
    return start to end
}

// Yarın için başlangıç ve bitiş timestamp'lerini döndürür
fun getTomorrowTimeRange(): Pair<Long, Long> {
    val calendar = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }
    val start = calendar.timeInMillis / 1000
    val end = start + 24 * 60 * 60 // 1 gün sonrası
    return start to end
}


// Bugün için verileri filtreler
fun WeatherForecastResponse.getTodayForecasts(): List<WeatherForecastItem> {
    val (start, end) = getTodayTimeRange()
    return this.list.filter {
        val timestamp = it.dateTime.toTimestamp() ?: 0L
        timestamp >= start && timestamp < end
    }.sortedBy { it.dateTime.toTimestamp() }.take(8)
}

// Yarın için verileri filtreler
fun WeatherForecastResponse.getTomorrowForecasts(): List<WeatherForecastItem> {
    val (start, end) = getTomorrowTimeRange()
    return this.list.filter {
        val timestamp = it.dateTime.toTimestamp() ?: 0L
        timestamp >= start && timestamp < end
    }.sortedBy { it.dateTime.toTimestamp() }
        .take(8)
}


fun List<WeatherForecastItem>.toWeatherCards(): List<WeatherCard> {
    return this.mapIndexed { index, forecast ->
        val timestamp = forecast.dateTime.toTimestamp() ?: 0L
        WeatherCard(
            time = convertTimestampToHour(timestamp),
            date = convertTimestampToDate(timestamp),
            temperature = "${forecast.main.temp.toInt()}°C",
            iconCode = forecast.weather?.firstOrNull()?.icon ?: "01d",
            isSelected = false
        )
    }
}