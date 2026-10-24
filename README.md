# AynaCast TV

An experimental AirPlay screen-mirroring receiver for Android TV, TV boxes, and tablets. On iPhone, open **Control Center → Screen Mirroring**; no companion iPhone app is required.

**Türkçe:** [README-TURKISH.md](README-TURKISH.md) · **Download:** [latest v1.0.0 debug APK](https://github.com/okarsli/aynacast-tv/releases/download/v1.0.0-debug/AynaCast-TV-1.0.0-debug.apk) · [Release notes](RELEASE_NOTES.md)

## What it does

- Advertises an AirPlay receiver that iPhone users can select from the built-in Screen Mirroring list.
- Includes Turkish strings and uses Android's selected app language/locale where available; English is the fallback.
- Provides receiver, video, audio, and per-client profile settings. Reported resolution and playback depend on the sender, Android decoder, device, and display.
- Does not upscale video or claim unsupported 8K, 120 Hz, or HDR output.

## Status and known limitations

This is a **debug / early-test release**, not a stable or broadly validated receiver. The APK installs and launches on an Android 11 TV, and an earlier test session connected an iPhone. The latest build has not yet been re-tested for AirPlay playback after its installation. Codec, frame rate, end-to-end latency, and HDR output have not been measured for this release.

Reported issues from earlier manual use include audio arriving late after volume changes and transient black screens or reconnects when moving between some short videos. DRM-protected video (including Netflix playback) may show a black screen; protected video playback is not supported. See [the release notes](RELEASE_NOTES.md) for the current test status.

## Install and connect

1. Download the debug APK from the [v1.0.0-debug release](https://github.com/okarsli/aynacast-tv/releases/tag/v1.0.0-debug) and install it on a compatible Android device.
2. Open AynaCast TV and start the receiver. Put the Android device and iPhone on the same local network.
3. On iPhone, open **Control Center → Screen Mirroring** and choose **AynaCast TV**.

Android may ask you to allow installation from the app used to open the APK. Only install APKs you trust. The application ID is `com.oguzhan.airplaytv`.

## Build from source

Clone the project with its native dependencies, then build a debug APK with JDK 21 and the Android SDK/NDK configured:

```sh
git clone --recurse-submodules https://github.com/okarsli/aynacast-tv.git
cd aynacast-tv
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`. The GitHub Actions workflow builds on Ubuntu. A clean Windows source build has not been validated as a release workflow.

## Upstream, license, and trademarks

AynaCast TV is based on [jqssun/android-airplay-server](https://github.com/jqssun/android-airplay-server) and [UxPlay](https://github.com/antimof/UxPlay), with AynaCast-specific changes. The Android receiver is licensed under GPL-3.0; see [LICENSE](LICENSE) and the license files included with native dependencies. AirPlay, iPhone, and Apple are trademarks of Apple Inc. AynaCast TV is an independent project and is not affiliated with or endorsed by Apple.
