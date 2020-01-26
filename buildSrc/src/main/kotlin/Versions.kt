internal object Versions {
    const val kotlin = "1.3.41"
    val androidx = "1.1.0"
    val retrofit = "2.3.0"
    val coreKtx = androidx
    val koin = "2.0.1"
    val junit = "4.12"
    val coroutines = "1.3.2"
    val constrainLayout = "1.1.3"
}

object Libs {
    val kotlinJDK = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val appcompat = "androidx.appcompat:appcompat:${Versions.androidx}"
    val coreKts = "androidx.core:core-ktx:${Versions.coreKtx}"
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constrainLayout}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val androidCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val junit = "junit:junit:${Versions.junit}"
}

object BuildPlugins {
    val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    val androidGradle = "com.android.tools.build:gradle:3.5.0"
}