package com.hopecoding.weatherapp.presentation.ui

import android.graphics.Paint
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.hopecoding.weatherapp.BuildConfig
import com.hopecoding.weatherapp.R
import com.hopecoding.weatherapp.data.model.WeatherCurrentResponse
import com.hopecoding.weatherapp.data.model.WeatherForecastItem
import com.hopecoding.weatherapp.databinding.ActivityMainBinding
import com.hopecoding.weatherapp.databinding.ItemIconDescriptionBinding
import com.hopecoding.weatherapp.domain.model.IconDescription
import com.hopecoding.weatherapp.presentation.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var weatherCardAdapter: WeatherCardAdapter
    private var isFirstResume = true
    private var wasInBackground = false
    private var isLocationPromptShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupWeatherCards()
        setupTabClickListeners()
        setupObservers()


        requestLocationPermission()
    }

    private fun setupTabClickListeners() {
        val tabs = listOf(binding.todayTab, binding.tomorrowTab, binding.fiveDaysTab)

        tabs.forEachIndexed { index, tab ->
            tab.setOnClickListener {
                viewModel.setSelectedTab(index)
            }
        }
    }

    private fun setupObservers() {
        setupTabObservers() // Sekme durumunu gözlemle

        viewModel.forecastData.observe(this) { forecastData ->
            Timber.d("ForecastData observed: %s", forecastData)
            if (forecastData != null) {
                binding.location.text = "${forecastData.city.name}, ${forecastData.city.country}"
            } else {
                binding.location.text = ""
            }
        }

        viewModel.currentWeather.observe(this) { currentWeather ->
            Timber.d("CurrentWeather observed: %s", currentWeather)
            if (currentWeather != null) {
                updateWeatherUI(currentWeather)
            } else {
                resetWeatherUI()
            }
        }

        viewModel.weatherCards.observe(this) { cards ->
            Timber.d("WeatherCards observed: %s", cards)
            if (cards != null && cards.isNotEmpty()) {
                weatherCardAdapter.updateCards(cards)
                binding.weatherCardsRecyclerView.visibility = View.VISIBLE
                binding.weatherCardsRecyclerView.scrollToPosition(0)
                updateIconDescriptions(viewModel.getUsedIconDescriptions())
                if (!weatherCardAdapter.hasSelectedCard()) {
                    viewModel.currentWeather.value?.let { updateWeatherUI(it) }
                }
            } else {
                binding.weatherCardsRecyclerView.visibility = View.GONE
                updateIconDescriptions(emptyList())
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showCustomToast(errorMessage)
            }
        }

        viewModel.showLocationPrompt.observe(this) { showPrompt ->
            if (showPrompt == true) {
                showLocationPrompt()
                viewModel.resetLocationPrompt()
            }
        }

    }

    private fun showLocationPrompt() {
        isLocationPromptShowing = true
        AlertDialog.Builder(this)
            .setTitle("Konum Servisleri Kapalı")
            .setMessage("Konum bilgileri kapalı. Konum ayarlarına gidilsin mi?")
            .setPositiveButton("Evet") { _, _ ->
                viewModel.locationProvider.openLocationSettings()
                if (viewModel.locationProvider.isLocationEnabled())
                    viewModel.checkAndFetchWeather()
                isLocationPromptShowing = false
            }
            .setNegativeButton("Hayır") { _, _ ->
                lifecycleScope.launch {
                    val location = viewModel.locationProvider.getLastKnownLocation()
                    if (location != null) {
                        val lat = location.latitude
                        val lon = location.longitude
                        viewModel.fetchCurrentWeather(lat, lon, viewModel.apiKey)
                        viewModel.fetchForecast(lat, lon, viewModel.apiKey)
                    } else {
                        showCustomToast("Son bilinen konum alınamadı.Null konum bilgisi döndü.")
                        resetUI()
                    }
                }
                isLocationPromptShowing = false
            }
            .setCancelable(false)
            .show()
    }


    private fun setupTabObservers() {
        lifecycleScope.launch {
            viewModel.selectedTab.collect { selectedIndex ->
                val tabs = listOf(binding.todayTab, binding.tomorrowTab, binding.fiveDaysTab)
                tabs.forEachIndexed { index, tab ->
                    if (index == selectedIndex) {
                        tab.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.orange))
                        tab.paintFlags = tab.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    } else {
                        tab.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                        tab.paintFlags = tab.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                    }
                }

                binding.weatherCardsRecyclerView.scrollToPosition(0)
                if (!weatherCardAdapter.hasSelectedCard()) {
                    viewModel.currentWeather.value?.let { updateWeatherUI(it) }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.weatherCardsRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupWeatherCards() {
        weatherCardAdapter = WeatherCardAdapter(
            onCardClick = { position ->
                if (weatherCardAdapter.hasSelectedCard()) {
                    viewModel.getSelectedForecast(position)?.let { updateWeatherUI(it) }
                        ?: showCustomToast("Veri yüklenemedi")
                } else {
                    viewModel.currentWeather.value?.let { updateWeatherUI(it) }
                }
            }
        )
        binding.weatherCardsRecyclerView.adapter = weatherCardAdapter
    }


    private fun requestLocationPermission() {
        val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    viewModel.checkAndFetchWeather()
                } else {
                    showCustomToast(getString(R.string.location_permission_denied))
                    resetUI()
                    viewModel.locationProvider.openLocationPermissions()
                }
            }
        requestPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }


    private fun updateIconDescriptions(icons: List<IconDescription>) {
        binding.iconDescriptionContainer.removeAllViews()

        if (icons.isEmpty()) {
            return
        }
        icons.forEach { icon ->
            val iconBinding = ItemIconDescriptionBinding.inflate(
                layoutInflater,
                binding.iconDescriptionContainer,
                false
            )

            val iconUrl = "https://openweathermap.org/img/wn/${icon.iconCode}@2x.png"
            iconBinding.iconImage.load(iconUrl) {
                crossfade(true)
                placeholder(R.drawable.sun)
                error(R.drawable.sun)
            }

            iconBinding.descriptionText.text = icon.description
            binding.iconDescriptionContainer.addView(iconBinding.root)
        }
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

    private fun resetWeatherUI() {
        binding.apply {
            temperature.text = ""
            feelsLike.text = ""
            sunsetTime.text = ""
            weatherIcon.setImageDrawable(null)
        }
    }

    private fun resetUI() {
        viewModel.resetData()
        resetWeatherUI()
        binding.location.text = ""
        binding.weatherCardsRecyclerView.visibility = View.GONE
    }


    private fun showCustomToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume called, isFirstResume: $isFirstResume")
        if (!isFirstResume && wasInBackground && !isLocationPromptShowing) {
            viewModel.checkAndFetchWeather()
        }
        isFirstResume = false
        wasInBackground = false
    }

    override fun onPause() {
        super.onPause()
        wasInBackground = true
    }

}