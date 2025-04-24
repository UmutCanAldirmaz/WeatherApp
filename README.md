# WeatherApp :cloud_with_rain:

WeatherApp is a modern UI weather application developed in Kotlin. It provides real-time weather information and 5-day forecasts based on the user's location, using the OpenWeatherMap API. The app leverages Hilt for dependency injection to ensure a clean architecture.

:book: [Türkçe README için tıklayın](README_TR.md)

## :rocket: Features

- **Current Weather**: Displays real-time temperature, feels-like temperature, and humidity based on the user's location.
- **Weather Forecast**: Provides forecasts for today, tomorrow, and the next 5 days.
- **Modern UI**: A user-friendly, tabbed interface (Today, Tomorrow, Next 5 Days).
- **Location Services**: Automatically detects the user's location and fetches weather data with location permission.
- **Error Handling**: Shows informative messages if location permission is denied or data cannot be retrieved.
- **Weather Icons**: Dynamically loads weather icons from OpenWeatherMap.

## :camera: Screenshots and Demo

Below, you can find screenshots of WeatherApp's interface and screen recordings demonstrating how the app works.

### Screenshots

<table>
  <tr>
    <th>Default Location</th>
    <th>Location Permission</th>
    <th>Location Settings</th>
    <th>Next 5 Days</th>
    <th>Card Selection</th>
    <th>Tab Selection</th>
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/338f520c-5ede-4442-a4a5-0541e45a3cc8" alt="Default Location" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/4142c49b-cc5c-4a99-b69f-1e8790e7d5c8" alt="Location Permission" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/0b1a7709-3ba7-4d4e-bda2-4b5bd69a4143" alt="Location Settings" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/226b6636-dc8b-494c-9980-5b9712021277" alt="Next 5 Days" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/b35a2c36-c3c2-4738-ba68-5082268d1df3" alt="Tab Selection" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/31c2ba53-70a3-4350-bc25-15398d1a69d2" alt="Tab Selection" width="150"/></td>
  </tr>
</table>

### Screen Recording

You can watch the screen recordings below to better understand how WeatherApp works:

[Screen_recording_weatherApp.webm](https://github.com/user-attachments/assets/e991cd95-bfa4-4aae-98e0-992899847348)

[Screen_recording_weatherApp2.webm](https://github.com/user-attachments/assets/6bbdb232-5f90-4de8-8afb-2b326f085d7a)

## :wrench: Technologies and Libraries Used

| Library/Category            | Version  | Description                                   |
|-----------------------------|----------|-----------------------------------------------|
| **Kotlin**                  | 2.0.21   | The primary programming language of the app.  |
| **Hilt**                    | 2.51.1   | For dependency injection.                     |
| **Retrofit**                | 2.11.0   | For HTTP requests and API integration.        |
| **Coil**                    | 2.5.0    | For loading weather icons.                    |
| **Play Services Location**  | 21.3.0   | For location services.                        |
| **Timber**                  | 5.0.1    | For logging operations.                       |
| **Coroutines**              | 1.7.3    | For asynchronous operations.                  |
| **LiveData & ViewModel**    | 2.8.7    | For state management and UI updates.          |
| **Material Design**         | 1.11.0   | For modern UI components.                     |
| **MVVM Architecture**       | -        | For better data management and UI separation. |

### Dependencies

The project's dependencies are defined in the `libs.versions.toml` file. Key dependencies include:

- androidx.appcompat:appcompat:1.6.1
- androidx.constraintlayout:constraintlayout:2.1.4
- com.squareup.retrofit2:retrofit:2.11.0
- com.google.dagger:hilt-android:2.51.1
- io.coil-kt:coil:2.5.0

## :package: Installation

To run WeatherApp on your local environment, follow the steps below:

### Requirements

- **Android Studio** (Latest version recommended)
- **Kotlin** 2.0.21
- **OpenWeatherMap API Key**: Obtain a free API key from [OpenWeatherMap](https://openweathermap.org/).

### Steps

1. **Clone the Project**:
   First, clone the project from GitHub and navigate to the project directory.

2. **Add the API Key**:
   Add the API key you obtained from OpenWeatherMap to the `local.properties` file. Add the following line to the file: WEATHER_API_KEY=your_api_key_here.

3. **Install Dependencies**:
   Open the project in Android Studio and run the "Sync Project with Gradle Files" option to install the dependencies.

4. **Run the App**:
   Connect an Android device or emulator. Then, click the "Run" button to launch the app.

### Note
Ensure that location permissions are enabled on the device to use location services. If location permission is denied, the app will attempt to use the default location.

## :book: Usage

When the app is launched, it will request location permission. Once granted:

1. **Current Weather**:
   The main screen displays real-time temperature, feels-like temperature, and humidity information.

2. **Tabs**:
   Access weather forecasts via the "Today," "Tomorrow," and "Next 5 Days" tabs.

3. **Weather Cards**:
   View hourly forecasts through horizontally scrollable cards.


## :memo: License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## :email: Contact

For questions or suggestions, feel free to reach out:  
:email: [ucan.aldirmaz@gmail.com](mailto:ucan.aldirmaz@gmail.com)  
:globe_with_meridians: [GitHub Profile](https://github.com/UmutCanAldirmaz)
