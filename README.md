# WeatherApp :cloud_with_rain:

WeatherApp is a modern UI weather application developed in Kotlin. It provides real-time weather information and 5-day forecasts based on the user's location, using the OpenWeatherMap API. The app leverages Hilt for dependency injection to ensure a clean architecture.

:book: [Türkçe README için tıklayın](README_TR.md)

## :rocket: Features

- **Current Weather**: Displays real-time temperature, "feels like" temperature, and humidity based on the user's location.
- **Weather Forecasts**: View forecasts for today, tomorrow, and the next 5 days.
- **Modern UI**: User-friendly tabbed interface (Today, Tomorrow, Next 5 Days).
- **Location Services**: Automatically detects the user's location with permission and fetches weather data.
- **Error Handling**: Informative messages for cases like denied location permissions or failed data retrieval.
- **Weather Icons**: Dynamically loads weather icons from OpenWeatherMap.

## :camera: Screenshots and Demo

Below are some screenshots of WeatherApp and a demo video showcasing its functionality.

### Screenshots

| Main Screen | Hourly Forecast | 5-Day Forecast |
|-------------|-----------------|----------------|
| ![Main Screen](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_1.png?raw=true) | ![Hourly Forecast](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_2.png?raw=true) | ![5-Day Forecast](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_3.png?raw=true) |

### Demo Video

Watch the demo below to see WeatherApp in action:

![WeatherApp Demo](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/weather_app_demo.gif?raw=true)

## :wrench: Technologies and Libraries

| Library/Category            | Version  | Description                                   |
|-----------------------------|----------|-----------------------------------------------|
| **Kotlin**                  | 2.0.21   | Main programming language.                   |
| **Hilt**                    | 2.51.1   | For dependency injection.                    |
| **Retrofit**                | 2.11.0   | For HTTP requests and API integration.       |
| **Coil**                    | 2.5.0    | For loading weather icons.                   |
| **Play Services Location**  | 21.3.0   | For location services.                       |
| **Timber**                  | 5.0.1    | For logging.                                 |
| **Coroutines**              | 1.7.3    | For asynchronous operations.                 |
| **LiveData & ViewModel**    | 2.8.7    | For state management and UI updates.         |
| **Material Design**         | 1.11.0   | For modern UI components.                    |

### Dependencies

The project's dependencies are defined in the `libs.versions.toml` file. Key dependencies include:

- `androidx.appcompat:appcompat:1.6.1`
- `androidx.constraintlayout:constraintlayout:2.1.4`
- `com.squareup.retrofit2:retrofit:2.11.0`
- `com.google.dagger:hilt-android:2.51.1`
- `io.coil-kt:coil:2.5.0`

## :package: Installation

To run WeatherApp on your local environment, follow these steps:

### Prerequisites

- **Android Studio** (Latest version recommended)
- **Kotlin** 2.0.21
- **OpenWeatherMap API Key**: Obtain a free API key from [OpenWeatherMap](https://openweathermap.org/).

### Steps

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/UmutCanAldirmaz/WeatherApp.git
   cd WeatherApp
