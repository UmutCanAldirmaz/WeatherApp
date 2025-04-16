package com.hopecoding.weatherapp.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.hopecoding.weatherapp.R
import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.databinding.ActivityMainBinding
import com.hopecoding.weatherapp.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var weatherCardAdapter: WeatherCardAdapter

    // Konum ayarları için ActivityResultLauncher
    private val locationSettingsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        // Konum ayarları ekranından dönüldü, konumu kontrol et
        viewModel.onLocationEnabled()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWeatherCards()
        setupTabClickListeners()
        setupObservers()

        viewModel.checkAndFetchWeather()
    }

    private fun setupRecyclerView() {
        binding.weatherCardsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupWeatherCards() {
        weatherCardAdapter = WeatherCardAdapter { position ->
            viewModel.getSelectedForecast(position)?.let { updateWeatherUI(it) }
                ?: Toast.makeText(this, "Veri yüklenemedi", Toast.LENGTH_SHORT).show()
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
                if (viewModel.forecastData.value != null) {
                    viewModel.setSelectedTab(index)
                    tabs.forEach { it.setTextColor(ContextCompat.getColor(this, R.color.gray)) }
                    tab.setTextColor(ContextCompat.getColor(this, R.color.white))
                    binding.todayTabIndicator.visibility =
                        if (index == 0) View.VISIBLE else View.INVISIBLE
                    if (index == 0) {
                        binding.todayTabIndicator.translationX = 0f
                    }
                }
            }
        }
    }

    private fun setupObservers() {
        viewModel.forecastData.observe(this) { forecastData ->
            if (forecastData != null) {
                binding.location.text = "${forecastData.city.name}, ${forecastData.city.country}"
            }
        }

        viewModel.currentWeather.observe(this) { currentWeather ->
            if (currentWeather != null) {
                updateWeatherUI(currentWeather)
            }
        }

        viewModel.weatherCards.observe(this) { cards ->
            // Veri geldiğinde RecyclerView'ı göster
            if (cards != null && cards.isNotEmpty()) {
                Log.d("MainActivity", "weatherCards observed: $cards")
                weatherCardAdapter.updateCards(cards)
                binding.weatherCardsRecyclerView.visibility = View.VISIBLE
            } else {
                binding.weatherCardsRecyclerView.visibility = View.GONE
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        // Yeni: Konum uyarısını dinle
        viewModel.showLocationPrompt.observe(this) { showPrompt ->
            if (showPrompt == true) {
                showLocationPrompt()
            }
        }
    }

    private fun showLocationPrompt() {
        AlertDialog.Builder(this)
            .setTitle("Konum Kapalı")
            .setMessage("Konum servisleri kapalı. Konum ayarlarını açmak ister misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                // Konum ayarlarını aç
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                locationSettingsLauncher.launch(intent)
            }
            .setNegativeButton("Hayır") { _, _ ->
                // Varsayılan konumu kullan
                viewModel.onLocationPromptDenied()
            }
            .setCancelable(false)
            .show()
    }

    private fun updateWeatherUI(data: Any) {
        binding.apply {
            when (data) {
                is WeatherForecastItem -> {
                    temperature.text = data.main.temp.toInt().toString()
                    feelsLike.text =
                        getString(R.string.feels_like_format, data.main.feelsLike.toInt())
                    sunsetTime.text = getString(R.string.humidity_format, data.main.humidity)

                    val iconCode = data.weather?.firstOrNull()?.icon
                    if (!iconCode.isNullOrEmpty()) {
                        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
                        weatherIcon.load(iconUrl) {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            error(R.drawable.ic_launcher_foreground)
                        }
                    }
                }

                is WeatherCurrentResponse -> {
                    temperature.text = data.main.temp.toInt().toString()
                    feelsLike.text =
                        getString(R.string.feels_like_format, data.main.feelsLike.toInt())
                    sunsetTime.text = getString(R.string.humidity_format, data.main.humidity)

                    val iconCode = data.weather?.firstOrNull()?.icon
                    if (!iconCode.isNullOrEmpty()) {
                        val iconUrl = "https://openweathermap.org/img/wn/$iconCode@2x.png"
                        weatherIcon.load(iconUrl) {
                            crossfade(true)
                            placeholder(R.drawable.ic_launcher_foreground)
                            error(R.drawable.ic_launcher_foreground)
                        }
                    }
                }
            }
        }
    }
}