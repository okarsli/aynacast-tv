# AynaCast TV

Android TV, TV box ve tabletler için deneysel bir AirPlay ekran yansıtma alıcısıdır. iPhone'da **Denetim Merkezi → Ekran Yansıtma** bölümünden bağlanılır; iPhone'a ek uygulama kurulmaz.

**English:** [README.md](README.md) · **İndir:** [v1.0.0 debug APK](https://github.com/okarsli/aynacast-tv/releases/download/v1.0.0-debug/AynaCast-TV-1.0.0-debug.apk) · [Sürüm notları](RELEASE_NOTES.md)

## Neler sunuyor?

- iPhone'un yerleşik Ekran Yansıtma listesinde seçilebilen bir AirPlay alıcısı sunar.
- Türkçe çeviri içerir; uygun olduğunda Android'in uygulama dili/yerel ayarını izler, varsayılan dil İngilizcedir.
- Alıcı, görüntü, ses ve istemci profili ayarları sağlar. Gerçek çözünürlük ve oynatma; göndericiye, Android çözücüye, cihaza ve ekrana bağlıdır.
- Görüntüyü yapay olarak büyütmez; desteklenmeyen 8K, 120 Hz veya HDR özelliklerini vaat etmez.

## Durum ve bilinen sınırlamalar

Bu sürüm **debug / erken test** sürümüdür; kararlı veya geniş ölçekte doğrulanmış değildir. APK Android 11 TV'ye kuruldu ve açıldı; önceki bir test oturumunda iPhone bağlantısı da kuruldu. Son derleme kurulduktan sonra AirPlay oynatması yeniden test edilmedi. Bu sürüm için codec, kare hızı, uçtan uca gecikme ve HDR çıkışı ölçülmedi.

Önceki elle kullanımda ses düzeyi değiştirildikten sonra sesin gecikmeli gelmesi ve bazı kısa videolar arasında geçişte geçici siyah ekran/yeniden bağlanma bildirildi. Netflix gibi DRM korumalı videolar siyah ekran gösterebilir; korumalı video oynatma desteklenmez. Güncel test durumu [sürüm notlarında](RELEASE_NOTES.md) yer alır.

## Kurulum ve bağlantı

1. [v1.0.0-debug sürümünden](https://github.com/okarsli/aynacast-tv/releases/tag/v1.0.0-debug) APK'yı indirin ve uyumlu Android cihaza yükleyin.
2. AynaCast TV'yi açıp alıcıyı başlatın. Android cihaz ile iPhone'u aynı yerel ağa bağlayın.
3. iPhone'da **Denetim Merkezi → Ekran Yansıtma**'yı açıp **AynaCast TV**'yi seçin.

Android, APK'yı açtığınız uygulama için bilinmeyen uygulama yükleme izni isteyebilir. Yalnızca güvendiğiniz APK'ları yükleyin. Uygulama kimliği `com.oguzhan.airplaytv` şeklindedir.

## Kaynak koddan derleme

Yerel bağımlılıklarla birlikte projeyi klonlayın; JDK 21 ve Android SDK/NDK yapılandırılmış olmalıdır:

```sh
git clone --recurse-submodules https://github.com/okarsli/aynacast-tv.git
cd aynacast-tv
./gradlew assembleDebug
```

APK `app/build/outputs/apk/debug/app-debug.apk` konumuna yazılır. GitHub Actions derlemeyi Ubuntu üzerinde yapar. Temiz bir Windows klonundan derleme yayın süreci olarak doğrulanmadı.

## Kaynak, lisans ve ticari markalar

AynaCast TV, AynaCast'e özel değişikliklerle [jqssun/android-airplay-server](https://github.com/jqssun/android-airplay-server) ve [UxPlay](https://github.com/antimof/UxPlay) temel alınarak geliştirilmiştir. Android alıcı kodu GPL-3.0 lisanslıdır; [LICENSE](LICENSE) ve yerel bağımlılıkların lisanslarına bakın. AirPlay, iPhone ve Apple, Apple Inc.'in ticari markalarıdır. AynaCast TV bağımsız bir projedir; Apple ile bağlantısı veya Apple onayı yoktur.
