plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

import java.util.Properties

val localProps = Properties().apply {
    rootProject.file("local.properties").takeIf { it.exists() }?.inputStream()?.use(::load)
}

val allAbis = providers.gradleProperty("targetAbis").orNull
    ?.split(',')
    ?.map { it.trim() }
    ?.filter { it.isNotEmpty() }
    ?.takeIf { it.isNotEmpty() }
    ?: listOf("arm64-v8a", "armeabi-v7a", "x86_64")

android {
    namespace = "io.github.jqssun.airplay"
    compileSdk = 37
    ndkVersion = "27.0.12077973"

    if (localProps.containsKey("storeFile")) {
        signingConfigs {
            create("release") {
                storeFile = file(localProps.getProperty("storeFile"))
                storePassword = localProps.getProperty("storePassword")
                keyAlias = localProps.getProperty("keyAlias")
                keyPassword = localProps.getProperty("keyPassword")
            }
        }
    }

    defaultConfig {
        applicationId = "com.oguzhan.airplaytv"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"

        externalNativeBuild {
            cmake {
                arguments += "-DANDROID_STL=c++_shared"
                arguments += "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON"
            }
        }
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        debug {
            ndk { abiFilters += allAbis }
        }
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfigs.findByName("release")?.let { signingConfig = it }
            ndk { abiFilters += allAbis }
        }
        // debuggable build with HWASan (arm64) + UBSan in native code
        create("sanitize") {
            initWith(getByName("debug"))
            matchingFallbacks += "debug"
            ndk {
                abiFilters.clear()
                abiFilters += "arm64-v8a"
            }
            externalNativeBuild {
                cmake { arguments += "-DSANITIZE=ON" }
            }
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        prefab = true
    }
}

tasks.register("applyUxplayPatches") {
    doLast {
        val uxplayRoot = file("src/main/cpp/third_party/UxPlay")
        if (!uxplayRoot.resolve(".git").exists()) {
            logger.lifecycle("Skipping UxPlay patch series because UxPlay is checked in as source files.")
            return@doLast
        }
        fun git(vararg args: String): String {
            val proc = ProcessBuilder("git", "-C", uxplayRoot.absolutePath, *args)
                .redirectErrorStream(true).start()
            val out = proc.inputStream.bufferedReader().readText()
            check(proc.waitFor() == 0) { "git ${args.joinToString(" ")} failed:\n$out" }
            return out
        }
        val patches = file("src/main/cpp/patches/UxPlay").listFiles { f -> f.extension == "patch" }!!.sorted()
        val touched = patches.flatMap { git("apply", "--numstat", it.path).trim().lines() }
            .map { it.substringAfterLast("\t") }.distinct()
        git("checkout", "--", *touched.toTypedArray())
        patches.forEach { git("apply", "--unidiff-zero", it.path) }
    }
}

tasks.register("applyOpenSslCmakePatches") {
    doLast {
        val opensslRoot = file("src/main/cpp/third_party/openssl-cmake")
        val patch = file("src/main/cpp/patches/openssl-cmake-windows.patch")

        val revisionProc = ProcessBuilder("git", "-C", opensslRoot.absolutePath, "rev-parse", "HEAD")
            .redirectErrorStream(true).start()
        val revision = revisionProc.inputStream.bufferedReader().readText().trim()
        check(revisionProc.waitFor() == 0 && revision == "4edd36a8dab5f85a8f92650b5bdf6e0cab13aab8") {
            "OpenSSL CMake submodule revision does not match the patch base: $revision"
        }

        fun gitApply(vararg args: String): Pair<Int, String> {
            val proc = ProcessBuilder("git", "-C", opensslRoot.absolutePath, "apply", *args)
                .redirectErrorStream(true).start()
            val out = proc.inputStream.bufferedReader().readText()
            return proc.waitFor() to out
        }

        val forward = gitApply("--unidiff-zero", "--check", patch.absolutePath)
        if (forward.first == 0) {
            val applied = gitApply("--unidiff-zero", patch.absolutePath)
            check(applied.first == 0) { "Could not apply OpenSSL CMake patch:\n${applied.second}" }
        } else {
            val reverse = gitApply("--unidiff-zero", "--reverse", "--check", patch.absolutePath)
            check(reverse.first == 0) {
                "OpenSSL CMake source does not match the expected patch base:\n${forward.second}"
            }
            logger.lifecycle("OpenSSL CMake host-compatibility patch is already applied.")
        }
    }
}

tasks.configureEach {
    if (name.startsWith("configureCMake")) {
        dependsOn("applyUxplayPatches", "applyOpenSslCmakePatches")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.datastore.prefs)
    implementation(libs.androidx.media)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.hls)
    implementation(libs.media3.ui.compose.material3)
    implementation(libs.media3.transformer)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.oboe)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
