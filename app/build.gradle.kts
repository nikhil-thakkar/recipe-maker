plugins {
    id("com.android.application")
    id("dev.nikhi1.plugin.android")
}

android {

    defaultConfig {
        applicationId = "dev.nikhi1.recipe.maker"
        versionCode = 1
        versionName = "1.0"
        resConfigs("en")
    }
}

dependencies {
    implementation(Libs.Design.constraintLayout)
    implementation(Libs.AndroidX.Navigation.dynamic)

    implementation(project(":home"))
    implementation(project(":onboarding"))
    implementation(project(":search"))
}