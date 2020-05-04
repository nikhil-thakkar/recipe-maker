object Versions {

    const val koin = "2.0.1"
    const val kotlin = "1.3.41"
    val coroutines = "1.3.2"

    object Android {
        val compileSDK = 29
        val minSDK = 21
        val targetSDK = compileSDK
        val buildTools = "29.0.2"
    }

    object AndroidX {
        val common = "1.2.0"
        val arch = "2.1.0"
        val coreKtx = common
        val navigation = "2.3.0-alpha04"

        object Lifecycle {
            const val common = "2.2.0"
        }
    }

    object Networking {
        val retrofit = "2.3.0"
        val gson = "2.8.6"
        val responseConverter = "2.8.1"
    }

    object Testing {
        val junit = "4.12"
        val mockk = "1.9.3"
        val coreTesting = AndroidX.arch
    }

    object Design {
        val material = "1.1.0-rc02"
        val constrainLayout = "1.1.3"
    }

    object PlayServices {
        val core = "1.6.5"
    }
}

object Libs {

    object AndroidX {
        val appcompat = "androidx.appcompat:appcompat:${Versions.AndroidX.common}"
        val coreKtx = "androidx.core:core-ktx:${Versions.AndroidX.common}"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

        object Lifecycle {
            val ext = "androidx.lifecycle:lifecycle-extensions:${Versions.AndroidX.Lifecycle.common}"
            val viewModel = "androidx.lifecycle:lifecycle-viewmodel:${Versions.AndroidX.Lifecycle.common}"
            val liveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.AndroidX.Lifecycle.common}"
            val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.AndroidX.Lifecycle.common}"
        }


        object Navigation {
            val navigator = "androidx.navigation:navigation-fragment-ktx:${Versions.AndroidX.navigation}"
            val dynamic = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.AndroidX.navigation}"
        }
    }

    object Networking {
        val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Networking.retrofit}"
        val gson = "com.google.code.gson:gson:${Versions.Networking.gson}"
        val responseConverter = "com.squareup.retrofit2:converter-gson:${Versions.Networking.responseConverter}"
    }

    object Testing {
        val junit = "junit:junit:${Versions.Testing.junit}"
        val archCore = "androidx.arch.core:core-testing:${Versions.Testing.coreTesting}"
        val corountines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutines}"
        val mockk = "io.mockk:mockk:${Versions.Testing.mockk}"
        val navTest = "androidx.navigation:navigation-testing:${Versions.AndroidX.navigation}"
    }

    object Design {
        val material = "com.google.android.material:material:${Versions.Design.material}"
        val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.Design.constrainLayout}"
    }

    object PlayServices {
        val playCore = "com.google.android.play:core:${Versions.PlayServices.core}"
    }

    val kotlinJDK = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
}