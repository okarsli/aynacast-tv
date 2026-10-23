# AynaCast TV

An Android AirPlay receiver for Android TVs, TV boxes, and tablets. iPhone users connect through the built-in **Screen Mirroring** control; no companion iPhone app is required.

Türkçe kullanım ve cihaz davranışı: [README-TURKISH.md](README-TURKISH.md).

## Development status

This is a development build. The APK has been built, but real iPhone-to-TV streaming has not yet been verified on the target TV. HDR output, 8K, and end-to-end latency are not claimed as tested.

The receiver adapts to capabilities reported by the Android device and the negotiated AirPlay stream. It does not upscale video. The achievable resolution and HDR mode depend on the sender, receiver hardware/decoder, and display. DRM-protected content such as Apple TV app video is not supported.

## Build

Clone with the remaining native dependencies, then build the debug APK:

```sh
git clone --recurse-submodules https://github.com/okarsli/aynacast-tv.git
cd aynacast-tv
./gradlew assembleDebug
```

On Windows, run `gradlew.bat assembleDebug`. The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

The modified UxPlay source is included directly in this repository. FFmpeg, libplist, and OpenSSL CMake remain Git submodules.

## Upstream and license

AynaCast TV is based on [jqssun/android-airplay-server](https://github.com/jqssun/android-airplay-server) and UxPlay, with AynaCast-specific changes. The Android receiver is licensed under GPL-3.0; see [LICENSE](LICENSE) and the license files in native dependencies.
