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

| Varsayılan Konum | Konum İzni | Sonraki 5 Gün (1) | Sonraki 5 Gün (2) | Sekme Seçimi | Hava Durumu Diyalogu |
|------------------|------------|-------------------|-------------------|--------------|----------------------|
| ![Varsayılan Konum]![Screenshot_defaultLocation](https://github.com/user-attachments/assets/338f520c-5ede-4442-a4a5-0541e45a3cc8)
 | ![Konum İzni](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/Screenshot_locationPermission.png) | ![Sonraki 5 Gün (1)](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/Screenshot_next5Days.png) | ![Sonraki 5 Gün (2)](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/Screenshot_next5Days2.png) | ![Sekme Seçimi](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/Screenshot_selectedTab.png) | ![Hava Durumu Diyalogu](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/Screenshot_weatherDialog.png) |

### Ekran Kaydı

WeatherApp'ın kullanımını daha iyi anlamak için aşağıdaki ekran kaydını izleyebilirsiniz:

![WeatherApp Ekran Kaydı](https://raw.githubusercontent.com/UmutCanAldirmaz/WeatherApp/main/Screenshots/weather_app_demo.gif)

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

Projenin bağımlılıkları libs.versions.toml dosyasında tanımlanmıştır. Başlıca bağımlılıklar şunlardır:

- androidx.appcompat:appcompat:1.6.1
- androidx.constraintlayout:constraintlayout:2.1.4
- com.squareup.retrofit2:retrofit:2.11.0
- com.google.dagger:hilt-android:2.51.1
- io.coil-kt:coil:2.5.0

## :package: Kurulum

WeatherApp'ı yerel ortamınızda çalıştırmak için aşağıdaki adımları izleyin:

### Gereksinimler

- **Android Studio** (En son sürüm önerilir)
- **Kotlin** 2.0.21
- **OpenWeatherMap API Anahtarı**: [OpenWeatherMap](https://openweathermap.org/) sitesinden ücretsiz bir API anahtarı alın.

### Adımlar

1. **Projeyi Klonlayın**:
   Öncelikle projeyi GitHub üzerinden klonlayın ve proje dizinine geçin.

2. **API Anahtarını Ekleyin**:
   OpenWeatherMap'ten aldığınız API anahtarını local.properties dosyasına ekleyin. Bunun için dosyaya WEATHER_API_KEY=your_api_key_here satırını ekleyin.

3. **Bağımlılıkları Yükleyin**:
   Android Studio'da projeyi açın ve bağımlılıkların yüklenmesi için Sync Project with Gradle Files seçeneğini çalıştırın.

4. **Uygulamayı Çalıştırın**:
   Bir Android cihaz veya emülatör bağlayın. Ardından Run butonuna tıklayarak uygulamayı çalıştırın.

### Not
Konum servislerini kullanmak için cihazda konum izninin açık olduğundan emin olun. Uygulama, konum izni verilmediğinde son bilinen konumu kullanmaya çalışır.

## :book: Kullanım

Uygulama açıldığında, konum izni isteyecektir. İzin verildikten sonra:

1. **Güncel Hava Durumu**:
   Ana ekranda anlık sıcaklık, hissedilen sıcaklık ve nem bilgisi görüntülenir.

2. **Sekmeler**:
   Bugün, Yarın ve Sonraki 5 Gün sekmeleri ile hava durumu tahminlerine erişebilirsiniz.

3. **Hava Durumu Kartları**:
   Yatay kaydırılabilir kartlar ile saatlik tahminleri görebilirsiniz.

### Örnek Kullanım

- Uygulamayı açtığınızda, konum izni verin.
- Ana ekranda mevcut konumunuzun, örneğin Bender Ereğli, TR, hava durumu bilgilerini göreceksiniz.
- Bugün sekmesinde saatlik tahminleri inceleyin:
  - Saat 12:00 - 14°C, Kapalı Bulutlu
  - Saat 15:00 - 13°C, Kapalı Bulutlu

## :handshake: Katkıda Bulunma

Katkıda bulunmak isterseniz:

1. Bu depoyu fork edin.
2. Yeni bir özellik branch'i oluşturun, örneğin feature/ozellik-adi adında bir branch.
3. Değişikliklerinizi yapın ve commit edin, örneğin Yeni özellik eklendi mesajıyla commit yapabilirsiniz.
4. Branch'inizi push edin, örneğin feature/ozellik-adi branch'ini origin'e push edin.
5. Bir Pull Request açın.

## :memo: Lisans

Bu proje MIT Lisansı altında lisanslanmıştır. Lisans detayları için LICENSE dosyasına bakabilirsiniz.

## :email: İletişim

Sorularınız veya önerileriniz için bana ulaşabilirsiniz:  
:email: [umutcan.aldirmaz@example.com](mailto:umutcan.aldirmaz@example.com)  
:globe_with_meridians: [GitHub Profilim](https://github.com/UmutCanAldirmaz)
