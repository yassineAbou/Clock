

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

@Suppress("UnstableApiUsage")
android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.clock"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

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
            isDebuggable = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.example.clock"
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.compose.ui:ui:${rootProject.extra.get("composeVersion")}")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.ui:ui-tooling-preview:${rootProject.extra.get("composeVersion")}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.lifecycle:lifecycle-service:2.6.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${rootProject.extra.get("composeVersion")}")
    debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra.get("composeVersion")}")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:${rootProject.extra.get("composeVersion")}")
    // compose-navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")
    // materiel3
    implementation("androidx.compose.material3:material3:1.1.0")
    // materiel-icon
    implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // systemUiController
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    // Hilt
    implementation("com.google.dagger:hilt-android:2.46.1")
    kapt("com.google.dagger:hilt-compiler:2.46.1")
    // Zhuinden flow-combinetuple
    implementation("com.github.Zhuinden:flow-combinetuple-kt:1.1.1")
    // Room
    implementation("androidx.room:room-ktx:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    // Swipe
    implementation("me.saket.swipe:swipe:1.1.1")
    // sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    // ssp
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    // gson
    implementation("com.google.code.gson:gson:2.10.1")
    // accompanist-permissions
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    // work-manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.0.0")
    // Desugaring
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
}
