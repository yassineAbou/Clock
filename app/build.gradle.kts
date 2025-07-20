

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 36

    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
        // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }

    defaultConfig {
        applicationId = "com.yassineabou.clock"
        minSdk = 24
        targetSdk = 36
        versionCode = 6
        versionName = "1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isDebuggable = true
        }
        register("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.yassineabou.clock"
}

dependencies {

    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.compose.ui:ui:1.8.3")
    implementation("androidx.compose.material:material:1.8.3")
    implementation("androidx.compose.ui:ui-tooling-preview:1.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.1")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-service:2.9.1")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.8.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.8.3")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.8.3")
    // compose-navigation
    implementation("androidx.navigation:navigation-compose:2.9.1")
    // materiel3
    implementation("androidx.compose.material3:material3:1.3.2")
    // materiel-icon
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    // systemUiController
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.56.2")
    kapt("com.google.dagger:hilt-compiler:2.56.2")
    // Zhuinden flow-combinetuple
    implementation("com.github.Zhuinden:flow-combinetuple-kt:1.3.0")
    // Room
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
    // Swipe
    implementation("me.saket.swipe:swipe:1.3.0")
    // sdp
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    // ssp
    implementation("com.intuit.ssp:ssp-android:1.1.1")
    // gson
    implementation("com.google.code.gson:gson:2.13.1")
    // accompanist-permissions
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")
    // work-manager
    implementation("androidx.work:work-runtime-ktx:2.10.2")
    implementation("androidx.hilt:hilt-work:1.2.0")
    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
}
