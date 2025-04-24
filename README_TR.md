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

<table>
  <tr>
    <th>Varsayılan Konum</th>
    <th>Konum İzni</th>
    <th>Konum Ayarları</th>
    <th>Sonraki 5 Gün</th>
    <th>Sekme Seçimi</th>
    <th>Sekme Seçimi</th>
    
  </tr>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/338f520c-5ede-4442-a4a5-0541e45a3cc8" alt="Varsayılan Konum" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/4142c49b-cc5c-4a99-b69f-1e8790e7d5c8" alt="Konum İzni" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/0b1a7709-3ba7-4d4e-bda2-4b5bd69a4143" alt="Konum Ayarları" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/226b6636-dc8b-494c-9980-5b9712021277" alt="Sonraki 5 Gün" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/b35a2c36-c3c2-4738-ba68-5082268d1df3" alt="Sekme Seçimi" width="150"/></td>
    <td><img src="https://github.com/user-attachments/assets/31c2ba53-70a3-4350-bc25-15398d1a69d2" alt="Sekme Seçimi" width="150"/></td>
  
  </tr>
</table>

### Ekran Kaydı

WeatherApp'ın kullanımını daha iyi anlamak için aşağıdaki ekran kaydını izleyebilirsiniz

[Screen_recording_weatherApp.webm](https://github.com/user-attachments/assets/e991cd95-bfa4-4aae-98e0-992899847348)

[Screen_recording_weatherApp2.webm](https://github.com/user-attachments/assets/6bbdb232-5f90-4de8-8afb-2b326f085d7a)












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


## :memo: Lisans

Bu proje MIT Lisansı altında lisanslanmıştır. Lisans detayları için LICENSE dosyasına bakabilirsiniz.

## :email: İletişim

Sorularınız veya önerileriniz için bana ulaşabilirsiniz:  
:email: [ucan.aldirmaz@gmail.com](mailto:ucan.aldirmaz@gmail.com)  
:globe_with_meridians: [GitHub Profilim](https://github.com/UmutCanAldirmaz)

