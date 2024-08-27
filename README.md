<div align="center">  
  <img src="https://i.imgur.com/6JQDkCX.png" alt="Weather" style="width: 100px; height: 100px; object-fit: contain; margin-right: 10px;">  
 <h1 style="display: inline-block; margin: 0; vertical-align: middle;">Clock</h1>  
</div>  

<p align="center">
  <a href="https://kotlinlang.org/">
    <img src="https://img.shields.io/badge/Kotlin-1.8.21-blue?logo=kotlin" alt="Kotlin version 1.8.21">
  </a>
  <a href="https://gradle.org/releases/">
    <img src="https://img.shields.io/badge/Gradle-8.0.2-white?logo=gradle" alt="Gradle version 8.0.2">
  </a>
  <a href="https://developer.android.com/studio">
    <img src="https://img.shields.io/badge/Android%20Studio-Flamingo-orange?logo=android-studio" alt="Android Studio Flamingo">
  </a>
  <a href="https://developer.android.com/studio/releases/platforms#7.0">
    <img src="https://img.shields.io/badge/Android%20min%20version-7-brightgreen?logo=android" alt="Android min version 7">
</a>
</p> 

Presenting a clock app which was totally made in Jetpack Compose! You can use this app's timer and stopwatch functions, which are linked to workManager.
The animation is added to the app to increase engagement. Also, you can create one time and recurring alarms and edit, enable, and disable them.

<p align="center">  
  <a href="https://play.google.com/store/apps/details?id=com.yassineabou.clock"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="80px"/></a>  
  <a href="https://apt.izzysoft.de/fdroid/index/apk/com.yassineabou.clock">
    <img alt="Get it on IzzyOnDroid" src="https://gitlab.com/IzzyOnDroid/repo/-/raw/master/assets/IzzyOnDroid.png" height="80px"/>
  </a>
</p>  

## Screenshots


<div style="display:flex; flex-wrap:wrap;">
  <img src="https://i.imgur.com/J74WooH.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/2HqKXxK.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/X3GSEDI.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/qxhtGdZ.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/qlilYzc.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/6vhlhsP.gif" style="flex:1; margin:5px;" height="450">
  <img src="https://i.imgur.com/OREtBGb.gif" style="flex:1; margin:5px;" height="450">
   <img src="https://i.imgur.com/ldPOuBV.gif" style="flex:1; margin:5px;" height="450">
     <img src="https://i.imgur.com/qfm8Vkm.gif" style="flex:1; margin:5px;" height="450">
</div>

## Features

<p>For full clock app functionality, permissions vary by Android version:</p>

<ul>
  <li>On Android 13+, grant notification permission for alarm, timer, and stopwatch alerts.</li>
  <li>On Android 12, If SCHEDULE_EXACT_ALARM isn't allowed, a dialog appears. Click "Ok" to be guided to the app settings where you can manually grant the permission.</li>
</ul>


## Architecture

The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [compose navigation](https://developer.android.com/jetpack/compose/navigation) to navigate between composables that are connected with the bottom navigation component
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

<p align="center"><a><img src="https://i0.wp.com/resocoder.com/wp-content/uploads/2018/08/mvvm-architecture-overview.png?w=492&ssl=1"></a></p>  

## Package Structure
```  
com.yassineabou.clock        # Root Package
├── data                 # For data operations
│   ├── local            # Handling local data
|   ├── manager          # Managing data operations
│   ├── model            # Data models
│   ├── receiver         # For receiving broadcasts
│   ├── repository       # Single source for data
│   └── workManager      # Handles background tasks
│       ├── factory      # For creating worker instances
│       └── worker       # Background task definitions
|  
├── di                   # Dependency Injection: Manages dependencies
│  
├── ui                   # UI Layer: Activity, Screens, ViewModels
│   ├── alarm            # Manages alarm functionalities
|   ├── stopwatch        # Manages stopwatch functionalities
|   ├── theme            # Handles app theme preferences
|   ├── timer            # Manages timer functionalities
|   ├── MainActivity     # Main activity hosting all fragments
│   └── Screen           # Manages different screens
|  
├── utils                # Utility Classes / Kotlin extensions for reusability
│   ├── components       # Reusable UI components
│   ├── helper           # Provides helper methods
|   ├── Extensions       # Kotlin extension functions
│   └── GlobalProperties # Global properties used across the app
└── ClockApplication     # Application class for global settings
```  


## Built With 🧰

- [Compose](https://developer.android.com/jetpack/compose) (Declarative UI framework)
- [Baseline Profiles](https://developer.android.com/topic/performance/baselineprofiles/overview) (Performance optimization tool)
- [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager) (Background task scheduling)
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation) (Dynamic UI navigation)
- [Flow](https://developer.android.com/kotlin/flow) (Asynchronous data streaming)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) (Observable state holder)
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) (Store and manage UI-related data)
- [Kotlin Coroutine](https://developer.android.com/kotlin/coroutines) (Light-weight threads)
- [Material Design 3](https://m3.material.io/) (Latest modern design component)
- [Materiel Icons](https://developer.android.com/jetpack/androidx/releases/compose-material) (Extended Material icons)
- [SystemUiController](https://google.github.io/accompanist/systemuicontroller/) (System UI bar colors management)
- [Hilt](https://dagger.dev/hilt/) (Dependency injection for android)
- [Flow-CombineTuple](https://github.com/Zhuinden/flow-combinetuple-kt) (Reactive streams combination utility)
- [Room](https://developer.android.com/topic/libraries/architecture/room) (Abstraction layer over SQLite)
- [Swipe](https://github.com/saket/swipe) (Swipe gesture actions for Compose)
- [Sdp](https://github.com/intuit/sdp) (Scalable size unit)
- [Ssp](https://github.com/intuit/ssp) (Scalable size unit for texts)
- [Gson](https://github.com/google/gson) (Java-to-JSON conversion library)
- [Accompanist Permissions](https://google.github.io/accompanist/permissions/) (Android runtime permissions for Compose)
- [Desugaring](https://developer.android.com/studio/write/java8-support) (Allowing apps to use newer Java libraries on older devices)



## Contribution
We welcome contributions to our project! Please follow these guidelines when submitting changes:

- Report bugs and feature requests by creating an issue on our GitHub repository.
- Contribute code changes by forking the repository and creating a new branch.
- Ensure your code follows our coding conventions.
- Improve our documentation by submitting changes as a pull request.

Thank you for your interest in contributing to our project!

## License
```
Copyright 2023 Yassine Abou 
  
Licensed under the Apache License, Version 2.0 (the "License");  
you may not use this file except in compliance with the License.  
You may obtain a copy of the License at  
  
    http://www.apache.org/licenses/LICENSE-2.0  
  
Unless required by applicable law or agreed to in writing, software  
distributed under the License is distributed on an "AS IS" BASIS,  
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
See the License for the specific language governing permissions and  
limitations under the License.
