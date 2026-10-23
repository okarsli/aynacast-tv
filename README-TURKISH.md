# AynaCast TV

Android TV, telefon ve tablet için, iPhone'un yerleşik AirPlay ekran yansıtmasını alır. iPhone'a ek uygulama kurulmaz.

## Kurulum ve bağlantı

1. APK'yı Android TV'ye, telefona veya tablete yükleyin ve uygulamayı bir kez açın.
2. Android cihaz ile iPhone'u aynı Wi-Fi ağına bağlayın.
3. iPhone'da Denetim Merkezi'ni açın, **Ekran Yansıtma**'ya dokunun ve **AynaCast TV**'yi seçin.
4. Uygulama yansıtma başlayınca tam ekrana geçer; bağlantı bitince normal ekrana döner.

## Otomatik ayarlar

- TV ekran çözünürlüğü, ekran yenileme hızı ve Android'in bildirdiği H.265/HDR özellikleri okunur. Ayarlar cihazın desteklediği sınırlar içinde seçilir.
- AirPlay istemcisinin cihaz kimliği yalnızca tek yönlü SHA-256 özetiyle tutulur. Aynı iPhone yeniden bağlanınca kayıtlı profili kullanır; yeni iPhone için ayrı profil açar.
- Düşük gecikme ayarı varsayılan olarak açıktır. Gerçek gecikme Wi-Fi ağına, iPhone'a ve TV'nin/cihazın işlem gücüne bağlıdır.
- Yansıtma tam ekran görüntülenir. Görüntü oranı korunur; kaynak ile ekran oranı farklıysa görüntü kesilmesin diye kenarlarda boşluk görülebilir.

## Çözünürlük, HDR ve sınırlar

Uygulama görüntüyü yapay olarak 8K'ya yükseltmez. En yüksek çözünürlük, iPhone'un AirPlay üzerinden gönderdiği akış, TV'nin paneli ve Android cihazdaki donanım çözücünün ortak sınırıdır. Profilo Android 11 4K HDR TV'de çıkış 4K'yı aşamaz; 8K ancak hem gönderici akışı hem TV/çözücü 8K destekliyorsa mümkündür. HDR de iPhone akışı, çözücü ve TV'nin HDR yolu destekliyorsa çalışır. Bu derleme gerçek TV üzerinde denenmediği için HDR geçişi ve 8K uçtan uca doğrulanmış sayılmaz.

Apple TV uygulaması gibi DRM korumalı içeriklerin AirPlay yansıtması desteklenmez.

## Kaynak ve lisans

Bu uygulama `jqssun/android-airplay-server` açık kaynak projesi (GPL-3.0) temel alınarak düzenlenmiştir. Değişiklikler: iPhone başına kayıtlı profil, TV/çözücü kapasitesine göre otomatik ayar, düşük gecikme varsayılanı, sürekli tam ekran ve `com.oguzhan.airplaytv` paket adı. Kaynak kod, alt modüller ve lisans bu pakettedir.

Upstream: https://github.com/jqssun/android-airplay-server
