# WeatherApp :cloud_with_rain:

WeatherApp, modern bir kullanıcı arayüzü ile tasarlanmış, Kotlin ile geliştirilmiş bir hava durumu uygulamasıdır. Kullanıcıların konumlarına göre güncel hava durumu bilgilerini ve 5 günlük hava durumu tahminlerini sunar. OpenWeatherMap API'sini kullanarak gerçek zamanlı veriler sağlar ve Hilt bağımlılık enjeksiyonu ile temiz bir mimari sunar.

## :rocket: Özellikler

- **Güncel Hava Durumu**: Kullanıcının konumuna göre anlık sıcaklık, hissedilen sıcaklık ve nem bilgisi.
- **Hava Durumu Tahminleri**: Bugün, yarın ve önümüzdeki 5 gün için hava durumu tahminleri.
- **Modern UI**: Kullanıcı dostu, sekmeli arayüz (Bugün, Yarın, 5 Gün).
- **Konum Servisleri**: Konum izni ile otomatik konum algılama ve hava durumu verisi çekme.
- **Hata Yönetimi**: Konum izni reddedildiğinde veya veri alınamadığında kullanıcıya bilgilendirici mesajlar.
- **Hava Durumu İkonları**: OpenWeatherMap'ten dinamik olarak yüklenen hava durumu ikonları.

## :camera: Ekran Görüntüleri ve Tanıtım

Aşağıda WeatherApp'ın arayüzüne dair ekran görüntülerini ve uygulamanın nasıl çalıştığını gösteren bir ekran kaydını bulabilirsiniz.

### Ekran Görüntüleri

| Ana Ekran | Saatlik Tahminler | 5 Günlük Tahmin |
|-----------|-------------------|-----------------|
| ![Ana Ekran](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_1.png?raw=true) | ![Saatlik Tahminler](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_2.png?raw=true) | ![5 Günlük Tahmin](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/Screenshot_3.png?raw=true) |

### Ekran Kaydı

WeatherApp'ın kullanımını daha iyi anlamak için aşağıdaki ekran kaydını izleyebilirsiniz:

![WeatherApp Ekran Kaydı](https://github.com/UmutCanAldirmaz/WeatherApp/blob/main/Screenshots/weather_app_demo.gif?raw=true)

## :wrench: Kullanılan Teknolojiler ve Kütüphaneler

| Kütüphane/Kategori          | Versiyon  | Açıklama                                      |
|-----------------------------|-----------|-----------------------------------------------|
| **Kotlin**                  | 2.0.21    | Uygulamanın ana programlama dili.            |
| **Hilt**                    | 2.51.1    | Bağımlılık enjeksiyonu için.                 |
| **Retrofit**                | 2.11.0    | HTTP istekleri ve API entegrasyonu.          |
| **Coil**                    | 2.5.0     | Hava durumu ikonlarını yüklemek için.        |
| **Play Services Location**  | 21.3.0    | Konum servisleri için.                       |
| **Timber**                  | 5.0.1     | Loglama işlemleri için.                      |
| **Coroutines**              | 1.7.3     | Asenkron işlemler için.                      |
| **LiveData & ViewModel**    | 2.8.7     | Durum yönetimi ve UI güncellemeleri için.    |
| **Material Design**         | 1.11.0    | Modern UI bileşenleri için.                  |

### Bağımlılıklar

Projenin bağımlılıkları `libs.versions.toml` dosyasında tanımlanmıştır. Başlıca bağımlılıklar:

- `androidx.appcompat:appcompat:1.6.1`
- `androidx.constraintlayout:constraintlayout:2.1.4`
- `com.squareup.retrofit2:retrofit:2.11.0`
- `com.google.dagger:hilt-android:2.51.1`
- `io.coil-kt:coil:2.5.0`

## :package: Kurulum

WeatherApp'ı yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyin:

### Gereksinimler

- **Android Studio** (En son sürüm önerilir)
- **Kotlin** 2.0.21
- **OpenWeatherMap API Anahtarı**: [OpenWeatherMap](https://openweathermap.org/) sitesinden ücretsiz bir API anahtarı alın.

### Adımlar

1. **Projeyi Klonlayın**:
   ```bash
   git clone https://github.com/UmutCanAldirmaz/WeatherApp.git
   cd WeatherApp
