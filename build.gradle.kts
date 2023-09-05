buildscript {
    val kotlinVersion by rootProject.extra { "1.8.21" }
    val composeVersion by rootProject.extra { "1.4.3" }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
    repositories {
        mavenCentral()
        google()
    }
} // Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.20" apply false
    id("com.android.test") version "8.0.2" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
