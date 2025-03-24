package com.hopecoding.weatherapp.presentation.ui.activities

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.hopecoding.weatherapp.R
import com.hopecoding.weatherapp.data.location.LocationProvider
import com.hopecoding.weatherapp.data.model.ForecastItem
import com.hopecoding.weatherapp.data.model.ForecastResponse
import com.hopecoding.weatherapp.databinding.ActivityMainBinding
import com.hopecoding.weatherapp.domain.model.WeatherCard
import com.hopecoding.weatherapp.presentation.WeatherCardAdapter
import com.hopecoding.weatherapp.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private var selectedTab = 0 // 0: Today, 1: Tomorrow, 2: Next 5 Days
    private lateinit var weatherCardAdapter: WeatherCardAdapter

    @Inject
    lateinit var locationProvider: LocationProvider

    private val apiKey = "eb293cb69baf127877adf77d28711842"

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val locationGranted = permissions.entries.all { it.value }
        if (locationGranted) {
            viewModel
        } else {
            val (lat, lon) = locationProvider.getDefaultLocation()
            viewModel.fetchCurrentWeather(lat, lon, apiKey)
            Toast.makeText(this, "Using default location", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWeatherCards()
        setupTabClickListeners()
        setupObservers()
        checkLocationPermission()
    }

    private fun setupRecyclerView() {
        binding.weatherCardsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupWeatherCards() {
        val weatherCards = listOf(
            WeatherCard("9 AM", "24°C", "01d"),
            WeatherCard("12 PM", "27°C", "01d", true),
            WeatherCard("3 PM", "21°C", "04d"),
            WeatherCard("6 PM", "20°C", "04d")
        )

        weatherCardAdapter = WeatherCardAdapter(weatherCards) { position ->
            updateWeatherDataForTime(position)
        }

        binding.weatherCardsRecyclerView.adapter = weatherCardAdapter
    }

    private fun setupTabClickListeners() {
        val tabs = listOf(
            binding.todayTab,
            binding.tomorrowTab,
            binding.fiveDaysTab
        )

        tabs.forEachIndexed { index, tab ->
            tab.setOnClickListener {
                selectedTab = index

                // Update tab colors and indicator
                tabs.forEach { it.setTextColor(ContextCompat.getColor(this, R.color.gray)) }
                tab.setTextColor(ContextCompat.getColor(this, R.color.white))

                // Show/hide indicator based on selected tab
                binding.todayTabIndicator.visibility =
                    if (index == 0) View.VISIBLE else View.INVISIBLE
                if (index == 0) {
                    binding.todayTabIndicator.translationX = 0f
                }

                // Update weather data
                updateWeatherData()
            }
        }
    }

    private fun updateWeatherData() {
        when (selectedTab) {
            0 -> {
                val (lat, lon) = locationProvider.getDefaultLocation()
                viewModel.fetchCurrentWeather(lat, lon, apiKey)
            }

            1, 2 -> { // Tomorrow and 5 Days
                val (lat, lon) = locationProvider.getDefaultLocation()
                viewModel.fetchForecast(lat, lon, apiKey)
            }
        }
    }

    private fun updateWeatherDataForTime(position: Int) {
        viewModel.forecastData.value?.let { forecastData ->
            val forecasts = when (selectedTab) {
                0 -> forecastData.forecastList.take(4)
                1 -> getTomorrowForecasts(forecastData)
                2 -> getFiveDayForecasts(forecastData)
                else -> forecastData.forecastList.take(4)
            }

            if (position < forecasts.size) {
                val selectedForecast = forecasts[position]
                updateMainWeatherDisplay(selectedForecast)
            }
        }
    }

    private fun setupObservers() {
        viewModel.forecastData.observe(this) { forecastData ->
            updateUI(forecastData)
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state if needed
            binding.weatherCardsRecyclerView.visibility =
                if (isLoading) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun updateUI(forecastData: ForecastResponse) {
        binding.location.text = "${forecastData.city.name}, ${forecastData.city.country}"

        val forecasts = when (selectedTab) {
            0 -> forecastData.forecastList.take(4)
            1 -> getTomorrowForecasts(forecastData)
            2 -> getFiveDayForecasts(forecastData)
            else -> forecastData.forecastList.take(4)
        }

        forecasts.firstOrNull()?.let { updateMainWeatherDisplay(it) }

        val weatherCards = forecasts.map { forecast ->
            WeatherCard(
                time = convertTimestampToHour(forecast.time),
                temperature = "${forecast.main.temperature.toInt()}°C",
                iconCode = forecast.weather.firstOrNull()?.icon ?: "01d",
                isSelected = false
            )
        }
        weatherCards.getOrNull(1)?.isSelected = true
        weatherCardAdapter.updateCards(weatherCards)
    }

    private fun updateMainWeatherDisplay(forecast: ForecastItem) {
        binding.apply {
            temperature.text = forecast.main.temperature.toInt().toString()
            feelsLike.text = getString(R.string.feels_like_format, forecast.main.feelsLike.toInt())
            sunsetTime.text = getString(R.string.humidity_format, forecast.main.humidity)

            if (forecast.weather.isNotEmpty()) {
                val iconUrl = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png"
                weatherIcon.load(iconUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_foreground)
                }
            }
        }
    }

    private fun getTomorrowForecasts(forecastData: ForecastResponse): List<ForecastItem> {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis / 1000

        return forecastData.forecastList.filter {
            it.time >= tomorrow && it.time < tomorrow + 24 * 60 * 60
        }.take(4)
    }

    private fun getFiveDayForecasts(forecastData: ForecastResponse): List<ForecastItem> {
        return forecastData.forecastList.groupBy {
            val date = Date(it.time * 1000)
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.get(Calendar.DAY_OF_YEAR)
        }.values.take(5).map { dayForecasts ->
            dayForecasts.firstOrNull { forecast ->
                val date = Date(forecast.time * 1000)
                val calendar = Calendar.getInstance()
                calendar.time = date
                calendar.get(Calendar.HOUR_OF_DAY) == 12
            } ?: dayForecasts.first()
        }
    }

    private fun convertTimestampToHour(timestamp: Long): String {
        val sdf = SimpleDateFormat("h a", Locale.getDefault())
        return sdf.format(Date(timestamp * 1000))
    }

    private fun checkLocationPermission() {
        when {
            locationProvider.hasLocationPermission() -> {
                viewModel.forecastData.value?.let { getTomorrowForecasts(forecastData = it) }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_rationale),
                    Toast.LENGTH_LONG
                ).show()
                requestLocationPermission()
            }

            else -> {
                requestLocationPermission()
            }
        }
    }

    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }


}