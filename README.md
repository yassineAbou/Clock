<div align="center">  
  <img src="https://drive.google.com/uc?export=view&id=1g7YekUp6Z96ZGCp-CE6iAB2YmrsZ6_Qw" alt="Weather" style="width: 100px; height: 100px; object-fit: contain; margin-right: 10px;">  
 <h1 style="display: inline-block; margin: 0; vertical-align: middle;">Clock</h1>  
</div>  

<p align="center">  
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-v1.8.0-blue.svg" alt="Kotlin"></a>  
<a href="#"><img src="https://img.shields.io/badge/Min%20Version-Android%207-green" alt="Min Version: Android 7"></a>
 <a href="https://gradle.org/releases/">  
  <img src="https://img.shields.io/badge/Gradle-8-red.svg"  
       alt="Gradle Version 8"  
       style="border-radius: 3px; padding: 2px 6px; background-color: #FF0000; color: #fff;">  
</a>  
</p>  

Presenting a clock app which was totally made in Jetpack Compose! You can use this app's timer and stopwatch functions, which are linked to workManager.
The animation is added to the app to increase engagement. Moreover, the time is shown in a stylish sliding clock manner.
But there's more than that! Also, you can create one time and recurring alarms and edit, enable, and disable them.

<p align="center">  
  <a href="https://github.com/yassineAbou"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" height="80px"/></a>  
 <a href="https://github.com/yassineAbou"><img alt="Get it on F-Droid" src="https://f-droid.org/badge/get-it-on.png" height="80px"/></a>  
</p>  

## Screenshots


<div style="display:flex; flex-wrap:wrap;">
  <img src="https://drive.google.com/uc?export=view&id=1KiBCosf9LPKXy_ZEUzrW73RPP2kWWIse" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1vT17-84-NOh3tVIwOx1cvyJ-hBvKafOQ" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1rU76yJAMFTWB7paQNIhAvTFh16G6LkfF" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1G-hiJdjIYlglO7M5n9VS5mYyqz4sAfOu" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1G1oZlTH4Nk5bRxgC35xZKDASGhF-GbyG" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1YyNBgOy0b2H5gNgB5MoW29trioK1JaYI" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=15zuI8U8CCsdEmpjqKVFOnu5k3Sp2bo7e" style="flex:1; margin:5px;" height="450">
  <img src="https://drive.google.com/uc?export=view&id=1IcezW7b7S0Cm86Y2sn9zlMBoBhWNxUdJ" style="flex:1; margin:5px;" height="450">
   <img src="https://drive.google.com/uc?export=view&id=1N7cMDpf1I2cKBVy1DpeFKF3R2vJ4BesK" style="flex:1; margin:5px;" height="450">
     <img src="https://drive.google.com/uc?export=view&id=1P7JV2olgle_FtgBRC-MH1czz_U2XrPx7" style="flex:1; margin:5px;" height="450">
</div>

## Architecture

The architecture of this application relies and complies with the following points below:
* A single-activity architecture, using the [compose navigation](https://developer.android.com/jetpack/compose/navigation) to navigate between composables that are connected with the bottom navigation component
* Pattern [Model-View-ViewModel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)(MVVM) which facilitates a separation of development of the graphical user interface.
* [Android architecture components](https://developer.android.com/topic/libraries/architecture/) which help to keep the application robust, testable, and maintainable.

<p align="center"><a><img src="https://i0.wp.com/resocoder.com/wp-content/uploads/2018/08/mvvm-architecture-overview.png?w=492&ssl=1"></a></p>  

## Package Structure
```  
com.example.clock       
├── data                     
│   ├── local              
|   ├── manager               
│   ├── model    
│   ├── receiver
│   ├── repository           
│   └── workManager                     
│       ├── factory          
│       └── worker           
|  
├── di                      
│  
├── ui                     
│   ├── alarm          
│   ├── clock     
|   ├── stopwatch      
|   ├── theme     
|   ├── timer         
|   ├── MainActivity         
│   └── Screen       
|  
├── utils   
│   ├── components          
│   ├── helper  
|   ├── Extensions         
│   └── GlobalProperties                
└── ClockApplication      
  
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