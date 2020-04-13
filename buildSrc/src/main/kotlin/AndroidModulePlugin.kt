import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/***
 * This plugin is instantiated every time when you apply the this plugin to build.gradle in feature module
 * <code>apply plugin: 'dev.nikhi1.plugin.android'</code>
 */
class AndroidModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (project.hasProperty("android")) {
            with(project) {
                plugins.apply("kotlin-android")
                plugins.apply("kotlin-android-extensions")
                configureAndroidBlock()
                if (name != "test_shared") {
                    configureCommonDependencies()
                }
                configureTestSharedDependencies()
                configureCoreModuleForOtherModules()
            }
        }
    }
}

internal fun Project.configureAndroidBlock() = extensions.getByType<BaseExtension>().run {

    buildToolsVersion(Versions.Android.buildTools)
    compileSdkVersion(Versions.Android.compileSDK)

    defaultConfig {
        minSdkVersion(Versions.Android.minSDK)
        targetSdkVersion(Versions.Android.targetSDK)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}

internal fun Project.configureCommonDependencies() {

    extensions.getByType<BaseExtension>().run {
        dependencies {
            add("implementation", Libs.Design.material)
            add("implementation", Libs.AndroidX.coreKtx)
            add("implementation", Libs.Networking.retrofit)
            add("implementation", Libs.koinViewModel)
            add("implementation", Libs.AndroidX.Lifecycle.ext)
            add("implementation", Libs.AndroidX.Lifecycle.viewModel)
            add("implementation", Libs.AndroidX.Lifecycle.viewModelKtx)
            add("implementation", Libs.AndroidX.Lifecycle.liveDataKtx)
            add("implementation", Libs.coroutines)
            add("implementation", Libs.AndroidX.coroutines)
            add("implementation", Libs.Networking.gson)

            if (name != "core") {
                add("implementation", Libs.AndroidX.Navigation.navigator)
            }
        }
    }
}

internal fun Project.configureTestSharedDependencies() {
    val core = findProject(":core")
    val testShared = findProject(":test_shared")
    val app = findProject(":app")

    extensions.getByType<BaseExtension>().run {
        dependencies {
            if (name == "test_shared") {
                if (core != null) {
                    add("implementation", core)
                }
                add("implementation", Libs.AndroidX.Lifecycle.ext)
                add("implementation", Libs.AndroidX.Lifecycle.viewModel)
                add("implementation", Libs.AndroidX.Lifecycle.viewModelKtx)
                add("implementation", Libs.AndroidX.Lifecycle.liveDataKtx)
            }
            if (testShared != null) {
                add("testImplementation", testShared)
            }

            add("testImplementation", Libs.Testing.junit)
            add("testImplementation", Libs.Testing.mockk)
            add("testImplementation", Libs.Testing.archCore)
            add("testImplementation", Libs.Testing.corountines)

            if (app != null && name != "app" && name != "core" && name != "test_shared") {
                add("androidTestImplementation", app)
            }
        }
    }
}

internal fun Project.configureCoreModuleForOtherModules() {
    if (name == "core") return
    val core = findProject(":core") as Project

    extensions.getByName("android").apply {
        when (this) {
            is BaseExtension -> {
                dependencies {
                    add("implementation", core)
                }
            }
        }
    }
}