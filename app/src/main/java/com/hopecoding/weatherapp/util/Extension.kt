package com.hopecoding.weatherapp.util

import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.domain.model.WeatherCard
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


private val sdfTimestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
private val sdfHour = SimpleDateFormat("HH:mm", Locale.getDefault())

fun convertTimestampToHour(timestamp: Long): String {
    return sdfHour.format(Date(timestamp * 1000))
}

fun String.toTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss"): Long? {
    return try {
        sdfTimestamp.parse(this)?.time?.div(1000)
    } catch (e: Exception) {
        null
    }
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

// Tarihin gününü döndürür
fun String.getDayOfYear(pattern: String = "yyyy-MM-dd HH:mm:ss"): Int {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = sdf.parse(this) ?: Date()
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.get(Calendar.DAY_OF_YEAR)
    } catch (e: Exception) {
        Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
    }
}

// Tarihin saatini döndürür
fun String.getHourOfDay(pattern: String = "yyyy-MM-dd HH:mm:ss"): Int {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        val date = sdf.parse(this) ?: Date()
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.get(Calendar.HOUR_OF_DAY)
    } catch (e: Exception) {
        0
    }
}
